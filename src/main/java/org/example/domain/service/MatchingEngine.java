package org.example.domain.service;

import org.example.domain.model.OrderStatus;
import org.example.domain.model.Side;
import org.example.domain.model.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.ArrayDeque;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

public class MatchingEngine {
    private static final Logger log = LoggerFactory.getLogger(MatchingEngine.class);
    OrderBook book;
    public MatchingEngine(){
         this.book = new OrderBook();
    }

    public OrderBook getBook() {
        return book;
    }
    //while (!book.asks.isEmpty() && book.getBestAsk().getKey().compareTo(order.getPrice()) <= 0)

    public void placeLimitOrder(Order order){
        try {
            TimeUnit.MILLISECONDS.sleep(0);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        //TODO turn place limit order into more methods in order to get readability
        Side newOrderSide = order.getSideOfOrder();
        int startQ = order.getQuantity();
        int remainingQ = order.getQuantity();
        if (newOrderSide.equals(Side.BUY)){
            Map.Entry<BigDecimal, ArrayDeque<Order>> bestAsk = book.getBestAsk();
            while(remainingQ>0 && bestAsk!=null && bestAsk.getKey().compareTo(order.getPrice())<=0)
            {
                    ArrayDeque<Order> levelQueue = bestAsk.getValue();

                    if (bestAsk.getKey().compareTo(order.getPrice()) > 0) break;

                    if (levelQueue == null || levelQueue.isEmpty()) {
                        book.removeEmptyLevelAsks(bestAsk.getKey());
                        bestAsk = book.getBestAsk();
                        continue;
                    }
                    Order top = levelQueue.peekFirst();

                    if(top ==null){
                        book.removeEmptyLevelAsks(bestAsk.getKey());
                        bestAsk = book.getBestAsk();
                        continue;
                    }
                    int executed = Math.min(top.getQuantity(),remainingQ);
                    remainingQ-=executed;
                    top.setQuantity(top.getQuantity() -executed);
                log.debug("Order matched{} with{}with price{}", top.getId(), order.getId(), top.getPrice());
                    if (top.getQuantity() == 0) {
                        top.setStatus(OrderStatus.FILLED);
                        levelQueue.pollFirst();
                    }
                    if (levelQueue.isEmpty()) {
                        book.removeEmptyLevelAsks(bestAsk.getKey());
                    }
                  bestAsk = book.getBestAsk();
            }
            if (remainingQ < startQ){
                order.setStatus(OrderStatus.PARTIALLY_FILLED);
            }
            if (remainingQ > 0) {

                order.setQuantity(remainingQ);
                book.addOrder(order);
            }


        }else {
            Map.Entry<BigDecimal, ArrayDeque<Order>> bestBid = book.getBestBid();
            while (remainingQ > 0 && bestBid != null && bestBid.getKey().compareTo(order.getPrice()) >= 0) {
                ArrayDeque<Order> levelQueue = bestBid.getValue();
                if (levelQueue == null || levelQueue.isEmpty()) {
                    book.removeEmptyLevelBids(bestBid.getKey());
                    bestBid = book.getBestBid();
                    continue;
                }
                Order top = levelQueue.peekFirst();
                bestBid = preCheck(top, bestBid);
                log.info(bestBid.getKey() + " " + bestBid.getValue().getFirst().getStatus());
                int executed = Math.min(top.getQuantity(), remainingQ);
                remainingQ -= executed;
                top.setQuantity(top.getQuantity() - executed);

                log.debug("Order matched {} with {} price {}", top.getId(), order.getId(), top.getPrice());

                if (top.getQuantity() == 0) {
                    top.setStatus(OrderStatus.FILLED);
                    levelQueue.pollFirst();
                }
                if (levelQueue.isEmpty()) {
                    book.removeEmptyLevelBids(bestBid.getKey());
                }

                bestBid = book.getBestBid();
            }
            if (remainingQ < startQ){
                order.setStatus(OrderStatus.PARTIALLY_FILLED);
            }
            if (remainingQ > 0) {

                order.setQuantity(remainingQ);
                book.addOrder(order);
            }
        }
    }
    private  Map.Entry<BigDecimal, ArrayDeque<Order>> preCheck(Order top , Map.Entry<BigDecimal, ArrayDeque<Order>> bestBid){
        if (top == null) {
            book.removeEmptyLevelBids(bestBid.getKey());
            return book.getBestBid();
        }else {
            log.info("preChecks completed");
            return bestBid;
        }
    }
    public void runEngine(){
        for (int i = 0; i < 1000000; i++) {
            Order order = new Order.Builder().build();
            this.placeLimitOrder(order);
            if (ThreadLocalRandom.current().nextInt(1,3)==2) {
                this.book.cancelOrder(order.getId());
            }
        }
    }
    public void run(){
        this.placeLimitOrder(new Order.Builder().addPrice(new BigDecimal(50)).addQuantity(50).addSide(Side.BUY).build());
        this.placeLimitOrder(new Order.Builder().addPrice(new BigDecimal(50)).addQuantity(50).addSide(Side.BUY).build());
        this.placeLimitOrder(new Order.Builder().addPrice(new BigDecimal(50)).addQuantity(150).addSide(Side.BUY).build());
        this.placeLimitOrder(new Order.Builder().addPrice(new BigDecimal(49)).addQuantity(200).addSide(Side.SELL).build());
        this.placeLimitOrder(new Order.Builder().addPrice(new BigDecimal(50)).addQuantity(200).addSide(Side.SELL).build());

    }
}

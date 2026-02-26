package org.example;

import org.jetbrains.annotations.NotNull;

import javax.swing.plaf.metal.MetalTheme;
import java.math.BigDecimal;
import java.util.ArrayDeque;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

public class MatchingEngine {
    OrderBook book;
    MatchingEngine(){
         this.book = new OrderBook();
    }

 //while (!book.asks.isEmpty() && book.getBestAsk().getKey().compareTo(order.getPrice()) <= 0)

    public void placeLimitOrder(Order order){
        Side newOrderSide = order.getSideOfOrder();
        int remainingQ = order.getQuantity();
        System.out.println(order.getPrice() +" " + order.getQuantity() +" " + order.getTimestamp() +" " + order.getSideOfOrder());
        try {
            TimeUnit.MILLISECONDS.sleep(1000);
        }catch (InterruptedException e){
            Thread.currentThread().interrupt();
        }

        if (newOrderSide.equals(Side.BUY)){


            System.out.println(order.getPrice() + " trying to fill buy order ");
            Map.Entry<BigDecimal, ArrayDeque<Order>> bestAsk = book.getBestAsk();
            while(remainingQ>0 && bestAsk!=null
                    && bestAsk.getKey().compareTo(order.getPrice())<=0){
                    ArrayDeque<Order> levelQueue = bestAsk.getValue();
                    if (bestAsk == null) break;
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
                    System.out.println("Order matched" + top.getId() + " with" + order.getId() + "with price" + top.getPrice());
                    if (top.getQuantity() == 0)
                        levelQueue.pollFirst();
                    if (levelQueue.isEmpty()) {
                        book.removeEmptyLevelAsks(bestAsk.getKey());
                    }
                  bestAsk = book.getBestAsk();
            }
            if (remainingQ > 0) {
                order.setQuantity(remainingQ);
                book.addOrder(order);
            }


        }else {
            System.out.println(order.getPrice() + " trying to fill sell order");

            Map.Entry<BigDecimal, ArrayDeque<Order>> bestBid = book.getBestBid();
            // Для sell: пока есть лучший бид >= цена продажи
            while (remainingQ > 0 && bestBid != null && bestBid.getKey().compareTo(order.getPrice()) >= 0) {
                ArrayDeque<Order> levelQueue = bestBid.getValue();
                if (levelQueue == null || levelQueue.isEmpty()) {
                    book.removeEmptyLevelBids(bestBid.getKey());
                    bestBid = book.getBestBid();
                    continue;
                }

                Order top = levelQueue.peekFirst();
                if (top == null) {
                    book.removeEmptyLevelBids(bestBid.getKey());
                    bestBid = book.getBestBid();
                    continue;
                }

                int executed = Math.min(top.getQuantity(), remainingQ);
                remainingQ -= executed;
                top.setQuantity(top.getQuantity() - executed);

                System.out.println("Order matched " + top.getId() + " with " + order.getId() + " price " + top.getPrice());

                if (top.getQuantity() == 0) {
                    levelQueue.pollFirst();
                }
                if (levelQueue.isEmpty()) {
                    book.removeEmptyLevelBids(bestBid.getKey());
                }

                bestBid = book.getBestBid();
            }

            if (remainingQ > 0) {
                order.setQuantity(remainingQ);
                book.addOrder(order);
            }
        }

        book.printBook();



    }
}

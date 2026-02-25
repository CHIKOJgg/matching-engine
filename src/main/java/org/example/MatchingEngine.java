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
            TimeUnit.MILLISECONDS.sleep(3000);
        }catch (InterruptedException e){
            Thread.currentThread().interrupt();
        }

        if (newOrderSide.equals(Side.BUY)){


            System.out.println(order.getPrice() + " trying to fill buy order ");
            Map.Entry<BigDecimal, ArrayDeque<Order>> bestAsk = book.getBestAsk();
            if (bestAsk != null &&
                    bestAsk.getKey().compareTo(order.getPrice()) <= 0) {
                book.printBook();
                while (remainingQ > 0) {
                    ArrayDeque<Order> bestLevelAsk = book.getBestAsk().getValue();
                    if (bestAsk == null) break;
                    if (bestAsk.getKey().compareTo(order.getPrice()) > 0) break;
                    ArrayDeque<Order> levelQueue = bestAsk.getValue();
                    Order currOrder =  levelQueue.peekFirst();
                    BigDecimal currOrderPrice =currOrder.getPrice();
                    int executed = Math.min(currOrder.getQuantity(), remainingQ);
                    remainingQ -= executed;
                    currOrder.setQuantity(currOrder.getQuantity() - executed);
                    System.out.println("Order matched" + currOrder.getId() + " with" + order.getId() + "with price" + currOrderPrice);
                    bestLevelAsk.pollFirst();
                    if (currOrder.getQuantity() == 0)
                        levelQueue.pollFirst();
                    if (bestLevelAsk.isEmpty()) {
                        book.removeEmptyLevelAsks(currOrderPrice);
                    }

                }

            } else {
                book.addOrder(order);

            }


        }else {
            Map.Entry<BigDecimal, ArrayDeque<Order>> bestBid = book.getBestBid();
                System.out.println(order.getPrice() +" trying to fill");
                if (bestBid != null&&book.getBestBid().getKey().compareTo(order.getPrice())<0){
                    while (remainingQ>0){
                    bestBid = book.getBestBid();
                        if (bestBid == null) break;
                        if (bestBid.getKey().compareTo(order.getPrice()) > 0) break;
                        ArrayDeque<Order> levelQueue = bestBid.getValue();
                        Order currOrder =  levelQueue.peekFirst();
                        BigDecimal currOrderPrice =currOrder.getPrice();

                      int executed = Math.min(currOrder.getQuantity(), remainingQ);
                        remainingQ -= executed;
                        currOrder.setQuantity(currOrder.getQuantity() - executed);
                        System.out.println("Order matched" + currOrder.getId() + " with" + order.getId() + "with price" + currOrderPrice);
                        levelQueue.pollFirst();
                        if (currOrder.getQuantity() == 0)
                            levelQueue.pollFirst();
                        if (levelQueue.isEmpty()) {
                            book.removeEmptyLevelBids(currOrderPrice);
                        }
                    }
                    book.printBook();
                }else {
                    book.addOrder(order);

                }
        }


    }
}

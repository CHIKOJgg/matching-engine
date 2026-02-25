package org.example;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalTime;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class OrderBook {
    TreeMap<BigDecimal, ArrayDeque<Order>> bids =new TreeMap<>(Comparator.reverseOrder());
    TreeMap<BigDecimal, ArrayDeque<Order>> asks =new TreeMap<>(Comparator.reverseOrder());
    void  addOrder(Order order){
       ;
       if (order.getSideOfOrder()==Side.SELL){
           asks.computeIfAbsent(order.getPrice(),
                   k->new ArrayDeque<>()).addLast(order);
       }
       else {
           bids.computeIfAbsent(order.getPrice(),
                   k ->new ArrayDeque<>()).addLast(order);
       }
    }

    public Map.Entry<BigDecimal,ArrayDeque<Order>> getBestBid(){
        if (bids.isEmpty())return  null;
       return bids.firstEntry();
    }
    public Map.Entry<BigDecimal, ArrayDeque<Order>> getBestAsk(){
        if (asks.isEmpty())return null;
        return asks.lastEntry();
    }
//    Order getOrder(String id){
//        return ordersMap.get(id);
//    }
//    void cancelOrder(String id){
//        ordersMap.remove(id);
//    }
    public void printBook(){
        printAsks();
        System.out.println("----------------");
        printBids();
    }
    private void printAsks(){
        System.out.println("-------asks----------");
        var listOfAsks = new ArrayList<>(asks.keySet());
        listOfAsks.reversed();
         for (BigDecimal price:listOfAsks){
             System.out.printf("ASK  | %5s | %d orders%n", price, asks.get(price).size());
         }

    }
    private void printBids(){

        var listOfBids = new ArrayList<>(bids.keySet());
        listOfBids.reversed();
        for (BigDecimal price:listOfBids){
            System.out.printf("BIDS  | %5s | %d orders%n", price, bids.get(price).size());
        }
        System.out.println("-------bids----------");
    }
    public void removeEmptyLevelBids(BigDecimal price) {
            if (bids.get(price).isEmpty()){
                bids.remove(price);
                System.out.println("bid level remove succeed");
            }
            else {
                System.out.println("bid level contains orders");
            }
    }
    public void removeEmptyLevelAsks(BigDecimal price) {
        if (asks.get(price).isEmpty()){
            asks.remove(price);
        }
    }
    public int getTotalQuantityAtLevel(BigDecimal price, Side side){
      try {
          if (side.equals(Side.BUY)){
              return bids.get(price).stream()
                      .mapToInt(Order::getQuantity).sum();

          }
          else {
              return asks.get(price).stream().mapToInt(Order::getQuantity).sum();
          }
      }catch (NullPointerException e){
          System.out.println("deque is empty");
          return -1;
      }
    }
    public Optional<BigDecimal> getBidAskSpread(){
       return Optional.ofNullable(getBestBid())
           .flatMap(bidEntry->
           Optional.ofNullable(getBestAsk())
           .map(
askEntry->askEntry.getKey()
        .subtract(bidEntry.getKey()))
       );
    }
//    public HashMap<String, Order> getOrdersMap() {
//        return ordersMap;
//    }

}

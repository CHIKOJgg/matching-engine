package org.example;

import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalTime;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Logger;

public class OrderBook {

    TreeMap<BigDecimal, ArrayDeque<Order>> bids =new TreeMap<>(Comparator.reverseOrder());
    TreeMap<BigDecimal, ArrayDeque<Order>> asks =new TreeMap<>(Comparator.reverseOrder());
    Map<String ,Order> orderIndex =new HashMap<>();
     synchronized void addOrder(Order order){
       orderIndex.put(order.getId(), order);
       if (order.getSideOfOrder()==Side.SELL){
           asks.computeIfAbsent(order.getPrice(),
                   k->new ArrayDeque<>()).addLast(order);
       }
       else {
           bids.computeIfAbsent(order.getPrice(),
                   k ->new ArrayDeque<>()).addLast(order);
       }
    }
    public void cancelOrder(String orderId){

        Order order = orderIndex.remove(orderId);
        if (order ==null){
            return;
        }

        ArrayDeque<Order> level;
        if (order.getSideOfOrder()==Side.BUY){
          level = bids.get(order.getPrice());
        }
        else {
          level = asks.get(order.getPrice());
        }

        if (order.getSideOfOrder() != null
                &&
            order.getSideOfOrder().equals(Side.BUY)
                &&
            level!=null)
        {
           boolean removed = level.removeIf(order1 -> {
               order1.setStatus(OrderStatus.CANCELLED);
                System.out.println("order" + orderId + "successfully deleted order");
                return order1.getId().equals(orderId);
            });
            if (removed && level.isEmpty()){
                bids.remove(order.getPrice());
            }



        }
        else if (order.getSideOfOrder() != null
                &&
                order.getSideOfOrder().equals(Side.SELL)
                &&
                level!=null){

          boolean removed =  level.removeIf(order1 -> {
                order1.setStatus(OrderStatus.CANCELLED);

                System.out.println("order" + orderId + "successfully deleted");
                return order1.getId().equals(orderId);
            });
            if(removed && level.isEmpty()){
                asks.remove(order.getPrice());
            }

        }
    }
    public synchronized  Map.Entry<BigDecimal,ArrayDeque<Order>>  getBestBid(){
        if (bids.isEmpty())return null;
       return bids.firstEntry();
    }
    public synchronized  Map.Entry<BigDecimal, ArrayDeque<Order>> getBestAsk(){
        if (asks.isEmpty())return null;
        return asks.lastEntry();
    }
//    Order getOrder(String id){
//        return ordersMap.get(id);
//    }
//    void cancelOrder(String id){
//        ordersMap.remove(id);
//    }
    public  synchronized  String printBook(){
       StringBuilder sbFinal = new StringBuilder();
       StringBuilder sbAsks = printAsks();
       StringBuilder sbBids = printBids();
       sbFinal.append(sbAsks);
       sbFinal.append("----------------").append("\n");;
       sbFinal.append(sbBids);
       return sbFinal.toString();
    }
    private StringBuilder printAsks(){
        StringBuilder sb = new StringBuilder();
        sb.append("--------asks----------").append("\n");
        var listOfAsks = new ArrayList<>(asks.keySet());
        listOfAsks.reversed();
         for (BigDecimal price:listOfAsks){
             sb.append("ASK  | ").append(price).append(" |").append(asks.get(price).size()).append(" orders").append("\n");
         }
         return sb;

    }
    private StringBuilder printBids(){
        StringBuilder sb =new StringBuilder();

        var listOfBids = new ArrayList<>(bids.keySet());
        listOfBids.reversed();
        for (BigDecimal price:listOfBids){
            sb.append("BIDS  | ").append(price).append(" |").append( bids.get(price).size()).append(" orders").append("\n");
        }
        System.out.println("-------bids----------");
        return sb;
    }
    public synchronized  void removeEmptyLevelBids(BigDecimal price) {
       var bidsGet = bids.get(price);
       if (bidsGet==null){
           System.out.println("ArrayDeque<order> is void at price: " + price);
           return;}

            if (bidsGet.isEmpty()){
                bids.remove(price);
                System.out.println("bid level remove succeed");
            }
            else {
                System.out.println("bid level contains orders");
            }
    }
    public synchronized  void removeEmptyLevelAsks(BigDecimal price) {
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

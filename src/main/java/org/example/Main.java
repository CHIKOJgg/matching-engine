package org.example;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        ComparatorForOrders comparatorForOrders = new ComparatorForOrders();
        CopmaratorForOrdersByTimestamp copmaratorForOrdersByTimestamp = new CopmaratorForOrdersByTimestamp();
        Comparator<Order> comparatorByPriceAndTimestamp = comparatorForOrders.thenComparing(copmaratorForOrdersByTimestamp);
        Comparator<Order> comparator1 = (o1, o2) -> o1.getPrice().compareTo(o2.getPrice());
        Comparator<Order> comparatorComparing =Comparator.comparing(Order::getPrice);
        TreeMap<BigDecimal, Order> asks = new TreeMap<>(Comparator.reverseOrder());
        IntStream.range(0,10).forEach(i->{
            Order order = createNewOrder(Side.SELL);
            asks.put(order.getPrice(), order);
        });

        TreeMap<BigDecimal, Order> bids = new TreeMap<>();
        IntStream.range(0,10).forEach(i->{
            Order order = createNewOrder(Side.SELL);
            bids.put(order.getPrice(), order);
        });
        for (BigDecimal price:asks.keySet()){;
            System.out.println(price);
        }
        System.out.println("best ask" + asks.lastKey());
        System.out.println("best bid" + bids.firstKey());
        for (BigDecimal price:bids.keySet()){;
            System.out.println(price);
        }

        List<Order> orderList = new ArrayList<>();






        OrderBook orderBook = new OrderBook();
        for (int i = 0; i <1_000_00 ; i++){
            orderBook.addOrder(
                    new Order(
                             "uid" + ThreadLocalRandom.current().nextInt(0,100000),
                            new BigDecimal(ThreadLocalRandom.current().nextLong(100,25000)),
                            ThreadLocalRandom.current().nextInt(),
                            ThreadLocalRandom.current().nextBoolean()?Side.BUY:Side.SELL,
                            LocalTime.now().getNano()
                    )
            );
        }
      //  IntStream.range(0,15).forEach(i-> System.out.println(orderBook.getOrdersMap().get()));
        HashMap<String, Order> orderHashMap = new HashMap<>();
        for (int i = 0; i < 10000; i++) {
            Order newOrder = new Order(
                    String.valueOf(i),
                    new BigDecimal(ThreadLocalRandom.current().nextInt(10, 1000)),
                    ThreadLocalRandom.current().nextInt(1, 100),
                    ThreadLocalRandom.current().nextBoolean() ? Side.BUY : Side.SELL,
                    LocalTime.now().getNano()
            );
            orderList.add(
                    newOrder
            );
            orderHashMap.put(
                    newOrder.getId(), newOrder
            );

        }
        List<String> randomIdForSearch = new ArrayList<>();
        for (int i = 0; i < 10000; i++) {
            randomIdForSearch.add(String.valueOf(ThreadLocalRandom.current().nextInt(1, 1000)));
        }


        orderList.sort(Comparator.comparing(Order::getPrice));
//        IntStream.range(0, orderList.size())
//                .forEach(i->orderHashMap.put(orderList.get(i).getId(),orderList.get(i)));

        long startOfHashMap = System.nanoTime();
        for (String id : randomIdForSearch) {
            orderHashMap.get(id);
        }
        long timeHM = System.nanoTime() - startOfHashMap;
        System.out.println("time for hashmap getting" + timeHM);


        long startOfList = System.nanoTime();

        for (String id : randomIdForSearch) {
            for (Order order : orderList) {
                if (order.getId().equals(id)) {
                    break;
                }
            }
        }
        long timeList = System.nanoTime() - startOfList;
        System.out.println("time for list getting" + timeList);

       // TreeMap<String, Order> treeMap = new TreeMap<>();
        //IntStream.range(0, orderList.size()).forEach(i->treeMap.put(orderList.get(i).getId(), orderList.get(i)));

       //   System.out.println( treeMap.firstKey());
       // System.out.println( treeMap.lastKey());
       // System.out.println(treeMap.size());
        //TreeMap<BigDecimal, Order> treeMap = new TreeMap<>(comparatorForOrders.thenComparing(copmaratorForOrdersByTimestamp));
        //IntStream.range(0, orderList.size()).forEach(i->treeMap.put(orderList.get(i).getPrice(), orderList.get(i)));
        findCheapestOrder(orderList);
        TreeMap<BigDecimal, Order> treeMapSell = new TreeMap<>();
        IntStream.range(0, 10).forEach(i->treeMapSell.put(orderList.get(i).getPrice(), orderList.get(i)));

    }
    private static void findCheapestOrder(List<Order> orderList){

        //тут уже лишняя сортировка?
     //   BigDecimal min = orderList.getFirst().getPrice();
        Order cheapest = orderList.getFirst();
        for (Order order : orderList) {
            if (order.getPrice().compareTo(cheapest.getPrice()) < 0) {
                cheapest = order;
            }
        }
        System.out.println(cheapest.getPrice());
        long startOfList = System.nanoTime();
        System.out.println(orderList.getFirst().getPrice());
        long timeList = System.nanoTime() - startOfList;
        System.out.println("time for list getting" + timeList);

    }
    private static Order createNewOrder(Side side){
        return new Order(
                "uid" + ThreadLocalRandom.current().nextInt(0,100000),
                new BigDecimal(ThreadLocalRandom.current().nextLong(100,25000)),
                ThreadLocalRandom.current().nextInt(),
                side,
                LocalTime.now().getNano());
    }

}
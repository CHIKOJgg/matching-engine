package org.example;

import java.math.BigDecimal;
import java.util.Deque;
import java.util.HashMap;
import java.util.TreeMap;

public class OrderBook {
    private  HashMap<String, Order> ordersMap = new HashMap<>();
    TreeMap<BigDecimal, Deque<Order>> bids;
    TreeMap<BigDecimal, Deque<Order>> asks;
    void  addOrder(Order order){
        ordersMap.put(order.getId(),order);
    }
    Order getOrder(String id){
        return ordersMap.get(id);
    }
    void cancelOrder(String id){
        ordersMap.remove(id);
    }

    public HashMap<String, Order> getOrdersMap() {
        return ordersMap;
    }
}

package org.example;

import java.util.HashMap;

public class OrderBook {
    private  HashMap<String, Order> ordersMap = new HashMap<>();
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

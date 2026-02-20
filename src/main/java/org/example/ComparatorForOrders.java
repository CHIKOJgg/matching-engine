package org.example;

import java.util.Comparator;

public class ComparatorForOrders implements Comparator<Order> {

    @Override
    public int compare(Order o1, Order o2) {
        return o1.getPrice().compareTo(o2.getPrice());
    }
}

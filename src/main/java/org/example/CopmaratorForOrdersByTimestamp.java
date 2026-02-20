package org.example;

import java.util.Comparator;

public class CopmaratorForOrdersByTimestamp implements Comparator<Order> {
    @Override
    public int compare(Order o1, Order o2) {
       return  Long.compare(o1.getTimestamp(), o2.getTimestamp());
    }
}

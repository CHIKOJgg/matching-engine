package org.example;

import javax.swing.*;
import java.math.BigDecimal;

public class Order implements Comparable<Order>{
    private String id;
    private BigDecimal price;
    private int quantity;
    private Side sideOfOrder;
    private long timestamp;

    public Order(String id, BigDecimal price, int quantity, Side sideOfOrder, long timestamp) {
        this.id = id;
        this.price = price;
        this.quantity = quantity;
        this.sideOfOrder = sideOfOrder;
        this.timestamp = timestamp;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public Side getSideOfOrder() {
        return sideOfOrder;
    }

    public void setSideOfOrder(Side sideOfOrder) {
        this.sideOfOrder = sideOfOrder;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    @Override
    public int compareTo(Order o) {
       return this.getPrice().compareTo(o.getPrice());

    }
}

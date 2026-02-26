package org.example;

import javax.swing.*;
import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.concurrent.ThreadLocalRandom;

public class Order implements Comparable<Order>{
    private String id;
    private BigDecimal price;
    private int quantity;
    private Side sideOfOrder;
    private long timestamp;
    int remainingQuantity;

    public Order(String id, BigDecimal price, int quantity, Side sideOfOrder, long timestamp) {
        this.id = id;
        this.price = price;
        this.quantity = quantity;
        this.sideOfOrder = sideOfOrder;
        this.timestamp = timestamp;
        this.remainingQuantity =  quantity;
    }

    public void setRemainingQuantity(int remainingQuantity) {
        this.remainingQuantity = remainingQuantity;
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
    public static BigDecimal bidPrice(){
        return new BigDecimal(ThreadLocalRandom.current().nextLong(32,38));
    } public static BigDecimal askPrice(){
        return new BigDecimal(ThreadLocalRandom.current().nextLong(26,32));
    }
    public static Order createNewOrder(Side side){
        return new Order(
                "uid" + ThreadLocalRandom.current().nextInt(0,100000),
                Side.SELL.equals(side)? bidPrice().multiply(new BigDecimal(5)):askPrice().multiply(new BigDecimal(5)),
                1,
                side,
                LocalTime.now().getNano());
    }

    @Override
    public int compareTo(Order o) {
       return this.getPrice().compareTo(o.getPrice());

    }
}

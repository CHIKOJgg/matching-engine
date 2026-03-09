package org.example;

import javax.swing.*;
import java.math.BigDecimal;
import java.security.PublicKey;
import java.time.LocalTime;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public class Order implements Comparable<Order>{

    private final String id;
    private final BigDecimal price;
    private  int quantity;
    private final Side sideOfOrder;
    private final long timestamp;
    int remainingQuantity;

    //Adding builder patter functionality
    private Order(Builder builder){
        this.id = builder.id;
        this.price = builder.price;
        this.quantity = builder.quantity;
        this.sideOfOrder = builder.sideOfOrder;
        this.timestamp = builder.timestamp;
        this.remainingQuantity =  builder.remainingQuantity;
    }

    public static class Builder{
        private String id  = String.valueOf(UUID.randomUUID());
        private BigDecimal price = new BigDecimal(ThreadLocalRandom.current().nextInt(25,52));
        private int quantity = ThreadLocalRandom.current().nextInt(1,100);
        private Side sideOfOrder = ThreadLocalRandom.current().nextBoolean()?Side.BUY:Side.SELL;
        private int remainingQuantity = this.quantity;
        private long timestamp;
        public Builder(String id){
            this.id = id;
        }
        public Builder addPrice(BigDecimal price){
            this.price = price;
            return this;
        }public Builder addQuantity(int quantity){
            this.quantity = quantity;
            return this;
        }public Builder addSide( Side side){
            this.sideOfOrder = side;
            return this;
        }
        public Order build(){
            return new Order(this);
        }


    }
    public Order(String id, BigDecimal price, int quantity, Side sideOfOrder, long timestamp) {
        this.id = id;
        this.price = price;
        this.quantity = quantity;
        this.sideOfOrder = sideOfOrder;
        this.timestamp = timestamp;
        this.remainingQuantity =  quantity;
    }

    public Order() {
        this.id = "templateID";
        this.price = new BigDecimal(123);
        this.quantity = 1;
        this.sideOfOrder = Side.BUY;
        this.timestamp = 123;
        this.remainingQuantity =  quantity;
    }
    public Order(BigDecimal price,Side side, int quantity) {
        this.id = "uid" + ThreadLocalRandom.current().nextInt(0,100000);
        this.price =price;
        this.quantity =quantity ;
        this.sideOfOrder = side;
        this.timestamp = LocalTime.now().getNano();
        this.remainingQuantity =  quantity;
    }
//TODO rebuild with constructors
    public static Order createNewOrder(Side side){
        return new Order(
                "uid" + ThreadLocalRandom.current().nextInt(0,100000),
                Side.SELL.equals(side)? bidPrice().multiply(new BigDecimal(5)):askPrice().multiply(new BigDecimal(5)),
                1,
                side,
                LocalTime.now().getNano());
    }
    public Order(Side side, int quantity) {
        this.id = "templateID";
        this.price = new BigDecimal(123);
        this.quantity = quantity;
        this.sideOfOrder = side;
        this.timestamp = 123;
        this.remainingQuantity =  this.quantity;
    }
    public Order(BigDecimal price, Side side) {
        this.id = "templateID";
        this.price =price;
        this.quantity = 1;
        this.sideOfOrder = side;
        this.timestamp = 123;
        this.remainingQuantity =  this.quantity;
    }

    public void setRemainingQuantity(int remainingQuantity) {
        this.remainingQuantity = remainingQuantity;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public Side getSideOfOrder() {
        return sideOfOrder;
    }
    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
//TODO разобраться как избежать использования
    public BigDecimal getPrice() {
        return price;
    }

    public String getId() {
        return id;
    }
    public static BigDecimal bidPrice(){
        return new BigDecimal(ThreadLocalRandom.current().nextLong(32,38));
    }

    public static BigDecimal askPrice(){
        return new BigDecimal(ThreadLocalRandom.current().nextLong(26,32));
    }


    @Override
    public int compareTo(Order o) {
       return this.getPrice().compareTo(o.getPrice());

    }
}

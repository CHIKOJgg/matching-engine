package org.example.ui;

import javafx.beans.property.SimpleStringProperty;

public class BookRow {

    private final SimpleStringProperty price;
    private final SimpleStringProperty volume;
    private final SimpleStringProperty orders;

    public BookRow(String price, String volume, String orders) {
        this.price = new SimpleStringProperty(price);
        this.volume = new SimpleStringProperty(volume);
        this.orders = new SimpleStringProperty(orders);
    }

    public String getPrice() {
        return price.get();
    }

    public String getVolume() {
        return volume.get();
    }

    public String getOrders() {
        return orders.get();
    }
}
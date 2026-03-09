package org.example;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.animation.AnimationTimer;
import javafx.beans.property.*;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ThreadLocalRandom;

public class OrderBookGui extends Application{
    private MatchingEngine matchingEngine;

    private TableView<BookRow> bidsTable = new TableView<>();
    private TableView<BookRow> asksTable = new TableView<>();


    public static void main(String[] args) {
        launch(args);
    }
    private void startUpdating() {
        AnimationTimer timer = new AnimationTimer() {
            private long lastUpdate = 0;
        @Override
        public void handle(long now) {
            if (now - lastUpdate>30_000_000){
                updateTables();
                lastUpdate = now;
            }
        }
    };
        timer.start();
    }

    private void updateTables() {

        List<BookRow> bids = new ArrayList<>();
        List<BookRow> asks = new ArrayList<>();
        synchronized (matchingEngine.book) {
            ArrayList<Integer> bidsVolumes = new ArrayList<>();
            ArrayList<Integer> asksVolumes = new ArrayList<>();;
            matchingEngine.book.bids.forEach((price, queue) -> {

                int volume = queue.stream()
                        .mapToInt(o -> o.getQuantity())
                        .sum();
                bidsVolumes.add(volume);
             //TODO refactor and implement orderbook depth with colors


                bids.add(new BookRow(
                        price.toString(),
                        String.valueOf(volume),
                        String.valueOf(queue.size())
                ));
            });

            //int maxVolumeBids = bidsVolumes.stream().mapToInt(i->i).max();
            matchingEngine.book.asks.forEach((price, queue) -> {

                int volume = queue.stream()
                        .mapToInt(o -> o.getQuantity())
                        .sum();
                asksVolumes.add(volume);
                asks.add(new BookRow(
                        price.toString(),
                        String.valueOf(volume),
                        String.valueOf(queue.size())
                ));
            });

           //int maxAskVol =  Collections.max(asksVolumes);
           //int maxBidVol =  Collections.max(bidsVolumes);

        }

        Platform.runLater(() -> {
            bidsTable.getItems().setAll(bids);
            asksTable.getItems().setAll(asks);
        });
    }
    @Override
    public void start(Stage stage) throws Exception {

        matchingEngine = new MatchingEngine();
        configureTable(bidsTable);
        configureTable(asksTable);
        Label bidsLabel= new Label("BIDS");
        Label asksLabel= new Label("ASKS");
        bidsLabel.setStyle("-fx-text-fill:#2ecc71; -fx-font-size:16px;");
        asksLabel.setStyle("-fx-text-fill:#e74c3c; -fx-font-size:16px;");
        VBox bidsBox= new VBox(5, bidsLabel,bidsTable);
        VBox asksBox = new VBox(5, asksLabel,asksTable);
        VBox tables = new VBox(10, asksBox, bidsBox);
        tables.setPadding(new Insets(10));
        Scene scene= new Scene(tables, 600,600);
        VBox.setVgrow(bidsTable, Priority.ALWAYS);
        VBox.setVgrow(asksTable, Priority.ALWAYS);
        // configureTable();

      //  textArea = new TextArea();
        //  textArea.setEditable(false);
//
//        textArea.setFont(javafx.scene.text.Font.font("Monospaced", 14));
//        textArea.setStyle("-fx-control-inner-background: #1a1a1a; -fx-text-fill: white;");
//        VBox root = new VBox(textArea);
//        Scene scene = new Scene(root, 500, 600);
        stage.setTitle("OrderBook");
        stage.setScene(scene);
        stage.show();
        startUpdating();
        Thread engineThread = new Thread(()->
            matchingEngine.runEngine()
        );
        engineThread.setDaemon(true);
        engineThread.start();

    }
    private void configureTable(TableView<BookRow> table) {
        TableColumn<BookRow,String> price = new TableColumn<>("Price");
        price.setCellValueFactory(c->new SimpleStringProperty(c.getValue().getPrice()));
        TableColumn<BookRow,String> volume = new TableColumn<>("Volume");
        volume.setCellValueFactory(c->new SimpleStringProperty(c.getValue().getVolume()));
        TableColumn<BookRow,String> orders = new TableColumn<>("Orders");
        orders.setCellValueFactory(c->new SimpleStringProperty(c.getValue().getOrders()));
        table.getColumns().addAll(price,volume,orders);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

}
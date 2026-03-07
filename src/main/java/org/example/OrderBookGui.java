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
    private TextArea textArea;



    public static void main(String[] args) {
        launch(args);
    }
    @Override
    public void start(Stage stage) throws Exception {

        matchingEngine = new MatchingEngine();
        textArea = new TextArea();
        textArea.setEditable(false);

        textArea.setFont(javafx.scene.text.Font.font("Monospaced", 14));
        textArea.setStyle("-fx-control-inner-background: #1a1a1a; -fx-text-fill: white;");
        VBox root = new VBox(textArea);
        Scene scene = new Scene(root, 500, 600);
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

    private void startUpdating() {
        AnimationTimer timer = new AnimationTimer() {
            private long lastUpdate = 0;
        @Override
        public void handle(long now) {
            if (now - lastUpdate>150_000_000){
                String booktext = matchingEngine.book.printBook();
                textArea.setText(booktext);
                lastUpdate = now;
            }
        }
    };
        timer.start();
    }

    private HBox createTopPanel() {
        HBox topPanel  = new HBox(20);
        topPanel.setPadding(new Insets(15));
        return topPanel;
    }
    private Node createOrderBookPanel(
    ) {
            GridPane gridPane = new GridPane();
            gridPane.setPadding(new Insets(15));
            Label bidsHeader = new Label("BID");
            Label asksHeader = new Label("ASK");
            Label priceHeader = new Label("PRICE");

            gridPane.add(bidsHeader, 0, 0, 2, 1);
            gridPane.add(asksHeader, 2, 0, 2, 1);

            var bidsContainer = new VBox(2);
            var asksContainer = new VBox(2);
            gridPane.add(bidsContainer,0,2,2,1);
            gridPane.add(asksContainer,2,2,2,1);
            return gridPane;

    }
    private HBox createBottomPanel() {
        HBox bottomPanel = new HBox(15);
        bottomPanel.setPadding(new Insets(10));
        return bottomPanel;
    }


}

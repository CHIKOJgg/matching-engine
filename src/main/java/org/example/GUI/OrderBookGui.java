package org.example.GUI;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.animation.AnimationTimer;
import javafx.beans.property.*;
import org.example.MatchingEngine;
import org.example.domains.Order;
import org.example.Side;

import java.math.BigDecimal;
import java.util.*;

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
        synchronized (matchingEngine.getBook()) {
            ArrayList<Integer> bidsVolumes = new ArrayList<>();
            ArrayList<Integer> asksVolumes = new ArrayList<>();
           // scanOrderBookAndCalcVolume();
            matchingEngine.getBook().bids.forEach((price, queue) -> {

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
            matchingEngine.getBook().asks.forEach((price, queue) -> {

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
        bidsLabel.getStyleClass().addAll("section-label", "bids-label");
        Label asksLabel= new Label("ASKS");
        asksLabel.getStyleClass().addAll("section-label", "asks-label");
        TextField priceField = new TextField();

        priceField.getStyleClass().add("input-label");
        TextField quantityField = new TextField();
        priceField.getStyleClass().add("input-label");

        Button buttonSell = createButton("SELL");
        buttonSell.setOnAction(actionButtonEventEventHandler(Side.SELL, priceField, quantityField));
        buttonSell.getStyleClass().addAll("button", "button-sell");

        Button buttonCancel = createButton("CANCEL");
        buttonCancel.getStyleClass().addAll("button", "button-cancel");

        Button buttonBuy = createButton("BUY");
        buttonBuy.setOnAction(actionButtonEventEventHandler(Side.BUY, priceField, quantityField));
        buttonBuy.getStyleClass().addAll("button", "button-buy");



        HBox controls = new HBox(10, new Label("Price"), priceField, new Label("Quantity"), quantityField, buttonBuy,buttonSell ,buttonCancel);
        controls.getStyleClass().add("controls-panel");
        controls.setPadding(new Insets(15));

        controls.setAlignment(Pos.CENTER_LEFT);

        bidsLabel.setStyle("-fx-text-fill:#2ecc71; -fx-font-size:16px;");
        asksLabel.setStyle("-fx-text-fill:#e74c3c; -fx-font-size:16px;");
        VBox bidsBox= new VBox(5, bidsLabel,bidsTable);
        VBox asksBox = new VBox(5, asksLabel,asksTable);
        VBox tables = new VBox(10, asksBox, bidsBox);
        VBox root  =new VBox(10, controls,tables);
        root.setPadding(new Insets(15));
        tables.setPadding(new Insets(10));
        Scene scene= new Scene(root, 1000,800);
        scene.getStylesheets().addAll(
                getClass().getResource("/styles/material-theme.css").toExternalForm(),
                getClass().getResource("/styles/material-fields.css").toExternalForm(),
                getClass().getResource("/styles/material-buttons.css").toExternalForm(),
                getClass().getResource("/styles/material-tables.css").toExternalForm()
        );
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
    private void  showAlert(String title, String message){
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message
        );
        alert.showAndWait();
    }
    private Button createButton(String buttonName) {
        Button button = new Button();
        button.setText(buttonName);
        return button;
    }
    private EventHandler<ActionEvent> actionButtonEventEventHandler(Side side, TextField priceField, TextField quantityField) throws NumberFormatException{
        return actionEvent -> {
            try{
                String priceText = priceField.getText().trim();
                String quantityText = quantityField.getText().trim();
                if (priceText.isEmpty()||quantityText.isEmpty())
                {
                    showAlert("Error" , "please fill fields");
                    return;
                }
                BigDecimal price = new BigDecimal(priceText);
                int quantity  = Integer.parseInt(quantityText);
                if (price.compareTo(BigDecimal.ZERO)<=0||quantity<=0){
                    showAlert("Error" , "price and quantity should be more than 0");
                    clearTextFields(priceField,quantityField);
                    return;
                }

                Order order = new Order.Builder().addPrice(price).addQuantity(quantity).addSide(side).build();
                matchingEngine.placeLimitOrder(order);
                clearTextFields(priceField,quantityField);
            }catch (NumberFormatException ex){
                showAlert("Error", "Wrong number format");
            }
        };
    }
    private void clearTextFields(TextField priceField, TextField quantityField ){
        priceField.clear();
        quantityField.clear();
    }

}
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

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

public class OrderBookGui extends Application {
    public void runMatchingEngine(){
        
    }
    @Override
    public void start(Stage stage) throws Exception {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #1a1a1a;");
        root.setTop(createTopPanel());
        root.setCenter(createOrderBookPanel());
        root.setBottom(createBottomPanel());
        Scene scene = new Scene(root, 500, 800);
        stage.setTitle("Matching engine");
        stage.setScene(scene);
        stage.show();
        
        runMatchingEngine();
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long l) {
                updateOrderBook();
            }
        };
        timer.start();
    }

    private void updateOrderBook() {
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
            Label askHeader = new Label("ASK");
            Label priceHeader = new Label("PRICE");

    }
    private Node createBottomPanel() {
    }


}

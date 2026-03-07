package org.example;

import javax.swing.*;
import java.awt.*;

public class OrderBookFrame extends JFrame {
    private JTextArea textArea;
    public OrderBookFrame(MatchingEngine engine){
        setTitle("Matching engine");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400,600);
        textArea = new JTextArea();
        textArea.setEditable(false);
        add(new JScrollPane(textArea));
        setVisible(true);
        new Thread(()->{
            while (true){
                updateDisplay(engine.book.printBook());
                try {
                    Thread.sleep(150);
                } catch (InterruptedException e) {
                   Thread.currentThread().interrupt();
                }
            }
        }).start();

    }
    private void updateDisplay(String context){
        SwingUtilities
        .invokeLater(()->textArea.setText(context));
    }

}

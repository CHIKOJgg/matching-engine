package org.example;

import org.example.ui.OrderBookGui;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        Logger logger = LoggerFactory.getLogger(Logger.class);
        logger.info("main is running");
        OrderBookGui.main(args);
    }
}
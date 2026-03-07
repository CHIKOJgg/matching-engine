package org.example;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.example.Order.bidPrice;
import static org.example.Order.createNewOrder;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        OrderBook orderBook = new OrderBook();
        MatchingEngine matchingEngine = new MatchingEngine();
        OrderBookFrame orderBookFrame = new OrderBookFrame(matchingEngine);
        OrderBookGui orderBookGui = new OrderBookGui();
    }


}
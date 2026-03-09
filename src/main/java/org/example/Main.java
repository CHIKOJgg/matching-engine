package org.example;
//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
       // OrderBook orderBook = new OrderBook();
        MatchingEngine matchingEngine = new MatchingEngine();
        OrderBookFrame orderBookFrame = new OrderBookFrame(matchingEngine);
        //OrderBookGui orderBookGui = new OrderBookGui();
    }
}
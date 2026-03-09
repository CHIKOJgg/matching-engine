import org.example.MatchingEngine;
import org.example.Order;
import org.example.Side;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.*;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class MatchingEngineTest {
    @Captor
    private ArgumentCaptor<Integer> argumentCaptor;
   // @Mock//куда вставляем
    private  MatchingEngine matchingEngine;

    public static Stream<Arguments> supplyOrdersVarargs() {
        return Stream.of(
                Arguments.of((Object) new Order[]{new Order(new BigDecimal(10), Side.SELL), new Order(new BigDecimal(9), Side.BUY)}),
                Arguments.of((Object) new Order[]{new Order(new BigDecimal(1), Side.SELL), new Order(new BigDecimal(0), Side.BUY)}),
                Arguments.of((Object) new Order[]{new Order(new BigDecimal("100.10"), Side.SELL), new Order(new BigDecimal("100.099"), Side.BUY)})
        );
    }


    @BeforeEach
    public void setUp(){
        this.matchingEngine = new MatchingEngine();
    }
    @Test
    public void deleteOrderTest(){
        matchingEngine.placeLimitOrder(new Order());
        Assertions.assertTrue(true);
       //Mockito.when(matchingEngine.placeLimitOrder(new Order())).then(true);
       // Mockito.doReturn(true).when(matchingEngine).placeLimitOrder(new Order());
    }
    public static Stream<Arguments> supplyBuyAndSellOrders(){
        return  Stream.of(
                Arguments.of(new Order.Builder().addSide(Side.BUY).build()),
                Arguments.of(new Order.Builder().addSide(Side.SELL).build())
        );
    }
    @ParameterizedTest
    @MethodSource("supplyBuyAndSellOrders")
    public void addOrdersReturnNonNUll(Order order){
        matchingEngine.placeLimitOrder(order);
        if (order.getSideOfOrder() ==Side.BUY){
            Assertions.assertFalse(matchingEngine.getBook().getBestBid().getValue().isEmpty());
        }else {
            Assertions.assertFalse(matchingEngine.getBook().getBestAsk().getValue().isEmpty());
        }
    }
    @Test
    public void nullCasesBookGetting(){
        Assertions.assertAll(
        ()-> Assertions.assertNull(matchingEngine.getBook().getBestBid()),
        ()-> Assertions.assertNull(matchingEngine.getBook().getBestAsk())
        );
    }
    @Test
    public void matchingOfOrders(){
        //
        matchingEngine.placeLimitOrder(new Order.Builder().addSide(Side.BUY).build());
        matchingEngine.placeLimitOrder(new Order.Builder().addSide(Side.SELL).build());

        var isEmptyBids = matchingEngine.getBook().getBestBid();
        var isEmptyAsks = matchingEngine.getBook().getBestAsk();
        org.assertj.core.api.Assertions.assertThat(isEmptyAsks).isEqualTo(null);
        org.assertj.core.api.Assertions.assertThat(isEmptyBids).isEqualTo(null);

    }
    public static Stream<Arguments> supplyOrders() {
        return Stream.of(
                //TODO refactor logic. returning null
//                Arguments.of(new Order(Side.BUY, 10), new Order(Side.SELL, 5)),
//                Arguments.of(new Order(Side.BUY, 10), new Order(Side.SELL, 5)),
//                Arguments.of(new Order(Side.BUY, 10), new Order(Side.SELL, 5)),
                Arguments.of(new Order(Side.BUY, 5), new Order(Side.SELL, 6)),
                Arguments.of(new Order(Side.BUY, 5), new Order(Side.SELL, 6)),
                Arguments.of(new Order(Side.BUY, 5), new Order(Side.SELL, 6))
        );
    }
    //recreating of dev branch
    @ParameterizedTest
    @MethodSource("supplyOrders")
    public void partialFillingMatchingOfOrders(Order orderBuy, Order orderSell){
        //
        matchingEngine.placeLimitOrder(orderBuy);
        matchingEngine.placeLimitOrder(orderSell);
        if (!matchingEngine.getBook().getBestAsk().getValue().isEmpty()){
           int q = matchingEngine.getBook().getBestAsk().getValue().getFirst().getQuantity();
           int testQuantity = orderSell.getQuantity() - orderBuy.getQuantity();
           Assertions.assertEquals(testQuantity, q, "order bid partially filled");
        }

        else if (!matchingEngine.getBook().getBestBid().getValue().isEmpty()) {
            int q = matchingEngine.getBook().getBestBid().getValue().getFirst().getQuantity();
            int testQuantity = orderBuy.getQuantity() - orderSell.getQuantity();
            Assertions.assertEquals(testQuantity, q,"order ask partially filled");
        }
        else {
           Assertions.fail();
        }
    }
    @Test
    public void fifoOrderExecution() throws InterruptedException {

        Order orderBuy5a = new Order.Builder()
                .addId("buy5a")
                .addPrice(new BigDecimal(100))
                .addQuantity(5).addSide(Side.BUY)
                .build();
        Order orderBuy5b = new Order.Builder()
                .addId("buy5a")
                .addPrice(new BigDecimal(100))
                .addQuantity(5).addSide(Side.BUY)
                .build();
        Order orderBuy5c = new Order.Builder()
                .addId("buy5c")
                .addPrice(new BigDecimal(100))
                .addQuantity(5).addSide(Side.BUY)
                .build();
        Order orderSell7 = new Order.Builder()
                .addId("sell15a")
                .addPrice(new BigDecimal(100))
                .addQuantity(15).addSide(Side.SELL)
                .build();

        matchingEngine.placeLimitOrder(orderBuy5a);
        matchingEngine.placeLimitOrder(orderBuy5b);
        matchingEngine.placeLimitOrder(orderBuy5c);
        matchingEngine.placeLimitOrder(orderSell7);

        Assertions.assertEquals(0, orderBuy5a.getQuantity());
        Assertions.assertEquals(3, matchingEngine.getBook().getBestBid().getValue().pollFirst().getQuantity());
        Assertions.assertEquals(5, matchingEngine.getBook().getBestBid().getValue().pollFirst().getQuantity());



    }
    @ParameterizedTest
    @MethodSource("supplyOrdersVarargs")
    public void noCrossWhenPricesDontMatch(Order... orders){
        List<Order> list = Arrays.stream(orders).toList();
        for (Order order : orders) {
        matchingEngine.placeLimitOrder(order);
        }
        Assertions.assertAll(
                ()-> org.assertj.core.api.Assertions
                .assertThat(matchingEngine.getBook()
                .getBestAsk().getValue().getFirst())
                .isEqualTo(list.get(0)),
                ()-> org.assertj.core.api.Assertions
                .assertThat(matchingEngine.getBook()
                .getBestBid().getValue().getFirst())
                .isEqualTo(list.get(1))

        );

    }
}

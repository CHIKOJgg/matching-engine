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
import org.mockito.internal.matchers.Or;
import java.util.stream.Stream;

public class MatchingEngineTest {
    @Captor
    private ArgumentCaptor<Integer> argumentCaptor;
   // @Mock//куда вставляем
    private  MatchingEngine matchingEngine;


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
                Arguments.of(new Order(Side.BUY)),
                        Arguments.of(new Order(Side.SELL))
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
        matchingEngine.placeLimitOrder(new Order(Side.BUY));
        matchingEngine.placeLimitOrder(new Order(Side.SELL));

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
}

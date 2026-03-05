import org.example.MatchingEngine;
import org.example.Order;
import org.example.OrderBookDao;
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

}

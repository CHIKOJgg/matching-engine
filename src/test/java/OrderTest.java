import com.sun.source.tree.AssertTree;
import org.example.Order;
import org.example.Side;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;

import java.io.IOException;
import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.time.Duration;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

import static org.mockito.Mockito.*;

public class OrderTest  extends  TestBase{

    static Order testOrderBuy;
    static Order testOrderSell;
    @BeforeEach
    public void setUpTestOrder(){
        testOrderSell = new Order("idofOrderSell", new BigDecimal(100), 1, Side.SELL, 100);
        testOrderBuy = new Order("idofOrderBuy", new BigDecimal(10), 1, Side.BUY, 100);
    }

    @MethodSource("supplyTestDataForNewOrderRaw")
    @ParameterizedTest
    @DisplayName("test of order constructor edge cases")
    public void OrderTestOfCreationNewOrder (String id, BigDecimal price, int q, Side side, long timestamp){
        Order order = new Order(id, price, q, side, timestamp);
    }
    @Test
    @DisplayName("performance")
    public void OrderPerformanceCreation(){
      Order orderOptional = Assertions.assertTimeout(Duration.ofMillis(100),
        ()-> {
            TimeUnit.MILLISECONDS.sleep(5);
            return new Order("idofOrder", new BigDecimal(0), 1, Side.SELL, 245);
      });
        if (orderOptional!=null){
            Assertions.assertTrue(true);
        }
        else {

            Assertions.fail();
        }
    }

    public static Stream<Arguments> supplyTestDataForNewOrderRaw() {
    return Stream.of(
            Arguments.of("", new BigDecimal(0), 1, Side.BUY, 245),
            Arguments.of("idofOrder", new BigDecimal(0), 1, Side.SELL, 245),
            Arguments.of("", new BigDecimal(1), 1, null, 245)
    );}
public static Stream<Arguments> supplyTestDataForNewOrder() {
    return Stream.of(
            Arguments.of(new Order("", new BigDecimal(0), 1, Side.BUY, 245)),
            Arguments.of(new Order("idofOrder", new BigDecimal(0), 1, Side.SELL, 245)),
            Arguments.of(new Order("", new BigDecimal("0.5"), 1, null, 245))
    );}
    @ParameterizedTest
    @MethodSource("supplyTestDataForNewOrder")
    public void testOrdersComparison (Order order) throws IOException {
        if (true) {
            throw new RuntimeException();
        }
        Order orderComparable = new Order("idofOrder", new BigDecimal(1), 1, Side.SELL, 245);
        int orderCompResult = orderComparable.compareTo(order);
        Assertions.assertEquals(1 , orderCompResult);

    }

    @Test
    void setRemainingQuantity() {
        testOrderBuy.setRemainingQuantity(12);
        Assertions.assertEquals(1, testOrderBuy.getQuantity());
    }

    @Test
    void getTimestamp() {
        testOrderBuy.setTimestamp(123);
        Assertions.assertEquals(123, testOrderBuy.getTimestamp());
    }

    @Test
    void setTimestamp() {

    }

    @Test
    void getSideOfOrder() {

    }

    @Test
    void setSideOfOrder() {
    }

    @Test
    void getQuantity() {
    }

    @Test
    void setQuantity() {
    }

    @Test
    void bidPrice() {
    }

    @Test
    void askPrice() {
    }

    @Test
    void createNewOrder() {
    }

    @Test
    void compareTo() {
    }
}


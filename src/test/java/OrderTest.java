import org.example.domains.Order;
import org.example.Side;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.Duration;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

public class OrderTest  extends  TestBase{

    static Order testOrderBuy;
    static Order testOrderSell;
    @BeforeEach
    public void setUpTestOrder(){
        testOrderSell = new Order.Builder()
                .addId("buy5a")
                .addPrice(new BigDecimal(100))
                .addQuantity(5)
                .addSide(Side.SELL)
                .build();
        testOrderBuy = new Order.Builder()
                .addId("buy5a")
                .addPrice(new BigDecimal(100))
                .addQuantity(5).addSide(Side.BUY)
                .build();
    }

    @MethodSource("supplyTestDataForNewOrderRaw")
    @ParameterizedTest
    @DisplayName("test of order constructor edge cases")
    public void OrderTestOfCreationNewOrder (String id, BigDecimal price, int q, Side side, long timestamp){
        Order order = new Order.Builder()
                .addId(id)
                .addPrice(price)
                .addQuantity(q).addSide(side)
                .build();
    }
    @Test
    @DisplayName("performance")
    public void OrderPerformanceCreation(){
      Order orderOptional = Assertions.assertTimeout(Duration.ofMillis(100),
        ()-> {
            TimeUnit.MILLISECONDS.sleep(5);
            return  new Order.Builder()
                    .addId("buy5a")
                    .addPrice(new BigDecimal(100))
                    .addQuantity(5).addSide(Side.BUY)
                    .build();
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
            Arguments.of(new Order.Builder()
                    .addId("idofOrder")
                    .addPrice(new BigDecimal(100))
                    .addQuantity(5).addSide(Side.BUY)
                    .build()),
            Arguments.of(new Order.Builder()
                    .addId("idofOrder")
                    .addPrice(new BigDecimal(0))
                    .addQuantity(1).addSide(Side.SELL)
                    .build()),
            Arguments.of(new Order.Builder()
                    .addId("buy5a")
                    .addPrice(new BigDecimal(100))
                    .addQuantity(5).addSide(Side.BUY)
                    .build())
    );}
    @ParameterizedTest
    @MethodSource("supplyTestDataForNewOrder")
    public void testOrdersComparison (Order order) throws IOException {
        Order orderComparable = new Order.Builder()
                .addId("idofOrder")
                .addPrice(new BigDecimal(101))
                .addQuantity(1).addSide(Side.SELL)
                .build();

        int orderCompResult = orderComparable.compareTo(order);
        Assertions.assertEquals(1, orderCompResult);

    }

    @Test
    void setRemainingQuantity() {
        testOrderBuy.setRemainingQuantity(12);
        Assertions.assertEquals(5, testOrderBuy.getQuantity());
    }
    @Test
    public void bidPriceTestShouldReturnFrom32to38(){
        org.assertj.core.api.Assertions.assertThat(Order.bidPrice()).isBetween(new BigDecimal(32),new BigDecimal(38));
    } @Test
    public void askPriceTestShouldReturnFrom26to32(){
        org.assertj.core.api.Assertions.assertThat(Order.askPrice()).isBetween(new BigDecimal(26),new BigDecimal(32));
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


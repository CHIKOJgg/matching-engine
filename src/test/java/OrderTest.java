import org.example.Order;
import org.example.Side;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.math.BigDecimal;
import java.util.stream.Stream;

public class OrderTest {
    @MethodSource("supplyTestDataForNewOrderRaw")
    @ParameterizedTest
    @DisplayName("test of order constructor edge cases")
    public void OrderTestOfCreationNewOrder (String id, BigDecimal price, int q, Side side, long timestamp){
        Order order = new Order(id, price, q, side, timestamp);
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
    public void testOrdersComparison (Order order){
        Order orderComparable = new Order("idofOrder", new BigDecimal(1), 1, Side.SELL, 245);
        int orderCompResult = orderComparable.compareTo(order);
        Assertions.assertEquals(1 , orderCompResult);

    }
}


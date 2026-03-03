import org.example.Order;
import org.example.OrderBook;
import org.example.Side;
import org.junit.jupiter.params.ParameterizedTest;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.ArrayDeque;

public class OrderBookTest {
    @ParameterizedTest
    void addOrderParametrized(Side side){
        OrderBook orderBook = new OrderBook();
        var arrDeq = new ArrayDeque<Order>();
        arrDeq.add(new Order("1", new BigDecimal(1), 1, side, LocalTime.now().getSecond()));

    }
}

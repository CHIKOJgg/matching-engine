import org.assertj.core.api.Assertions;
import org.example.MatchingEngine;
import org.example.Order;
import org.example.OrderBook;
import org.example.OrderBookDao;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.*;
import org.testng.annotations.Test;

public class MatchingEngingeTest {
    @Captor
    private ArgumentCaptor<Integer> argumentCaptor;
    @InjectMocks//куда вставляем
    private  MatchingEngine matchingEngine = new MatchingEngine();
    @Mock//мок
    private final OrderBookDao orderBookDao= new OrderBookDao();
     @Spy//мок
    private final OrderBookDao orderBookDaoSpy= new OrderBookDao();

    @Test

    public void deleteOrderTest(){
        matchingEngine.placeLimitOrder(new Order());
       //Mockito.when(matchingEngine.placeLimitOrder(new Order())).then(true);
       // Mockito.doReturn(true).when(matchingEngine).placeLimitOrder(new Order());
    }

}

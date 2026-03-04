import extensions.CondtionExtension;
import extensions.GlobalExtension;
import extensions.PostProcesstingExtension;
import extensions.ThrowableExtension;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith({
        GlobalExtension.class,
        PostProcesstingExtension.class,
        CondtionExtension.class,
        ThrowableExtension.class,
        MockitoExtension.class
})
public  abstract class TestBase {
}

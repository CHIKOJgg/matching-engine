package extensions;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestInstancePostProcessor;

public class PostProcesstingExtension implements TestInstancePostProcessor {
    @Override
    public void postProcessTestInstance(Object testInstance, ExtensionContext context) throws Exception {
        System.out.println("post process (di stage)");
        var declaredFields  = testInstance.getClass().getDeclaredFields();
//        for (Field declaredField : declaredFields) {
//            if (declaredField.isAnnotationPresent(Getter.class)){
//                declaredField.set(testInstance,new BigDecimal(1));
//            }
//        }
    }
}

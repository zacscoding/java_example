package proxy;

import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import org.junit.Test;
import util.SimpleLogger;

/**
 * @author zacconding
 * @Date 2018-03-01
 * @GitHub : https://github.com/zacscoding
 */
public class DynamicProxyTest {

    @Test
    public void mapTest() {
        Map mapProxyInstance = (Map) Proxy.newProxyInstance(DynamicProxyTest.class.getClassLoader(), new Class[]{Map.class}, new TimingDynamicInvocationHandler(new HashMap<>()));
        mapProxyInstance.put("TestKey", "TestValue");
        mapProxyInstance.clear();
    }

    @Test
    public void mapTestWithLambda() {
        Map target = new HashMap<>();
        Map mapProxyInstance = (Map) Proxy.newProxyInstance(DynamicProxyTest.class.getClassLoader(), new Class[]{Map.class}, (proxy, method, args) -> {
            long start = System.nanoTime();
            Object result = method.invoke(target, args);
            long elapsed = System.nanoTime() - start;
            SimpleLogger.println("[## Executing {}::{}] args : {}, return : {}, elapsed : {} ns", target.getClass().getName(), method.getName(), Arrays.toString(args), result, elapsed);
            return result;
        });

        mapProxyInstance.put("TestKey", "TestValue");
        mapProxyInstance.size();
    }
}

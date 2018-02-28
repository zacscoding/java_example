package proxy;

import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;
import org.junit.Test;

/**
 * @author zacconding
 * @Date 2018-03-01
 * @GitHub : https://github.com/zacscoding
 */
public class DynamicProxyTest {

    @Test
    public void mapTest() {
        Map mapProxyInstance = (Map) Proxy.newProxyInstance(DynamicProxyTest.class.getClassLoader(), new Class[]{Map.class}, new TimingDynamicInvocationHandler(new HashMap<>()));
        mapProxyInstance.put("TestKey","TestValue");
    }

}

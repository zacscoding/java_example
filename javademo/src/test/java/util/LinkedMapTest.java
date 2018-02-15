package util;

import java.util.LinkedHashMap;
import java.util.Map;
import org.junit.Test;

/**
 * @author zacconding
 * @Date 2018-02-16
 * @GitHub : https://github.com/zacscoding
 */
public class LinkedMapTest {

    @Test
    public void test() {
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put("key1", "value1");
        map.put("key3", "value3");
        map.put("key5", "value5");
        map.put("key4", "value4");

        map.forEach((k, v) -> {
            SimpleLogger.println("Key : {}, Value : {}", k, v);
        });
    }
}

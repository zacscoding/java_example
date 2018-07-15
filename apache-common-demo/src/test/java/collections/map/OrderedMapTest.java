package collections.map;

import static org.junit.Assert.assertTrue;

import org.apache.commons.collections4.OrderedMap;
import org.apache.commons.collections4.map.LinkedMap;
import org.junit.Test;
import util.DemoTask;
import util.SimpleLogger;

/**
 * @author zacconding
 * @Date 2018-07-15
 * @GitHub : https://github.com/zacscoding
 */
public class OrderedMapTest {


    @Test
    public void test() {
        DemoTask.doTask("Ordered Map", this::orderedMap);
    }

    private void orderedMap() {
        OrderedMap<Integer, String> map = new LinkedMap<>();
        map.put(2, "b");
        map.put(1, "a");
        map.put(3, "c");

        Integer key = map.firstKey();
        int idx = 0;
        do {
            String value = map.get(key);
            Integer keyByIndexed = ((LinkedMap<Integer, String>) map).get(idx);

            assertTrue(key.equals(keyByIndexed));

            SimpleLogger.println("Key : {} | Value : {}", key, value);
            idx++;
        } while ((key = map.nextKey(key)) != null);
    }
}

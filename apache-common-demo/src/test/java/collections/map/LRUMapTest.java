package collections.map;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.stream.IntStream;
import org.apache.commons.collections4.map.LRUMap;
import org.junit.Test;
import util.DemoTask;
import util.SimpleLogger;

/**
 * @author zacconding
 * @Date 2018-07-15
 * @GitHub : https://github.com/zacscoding
 */
public class LRUMapTest {

    @Test
    public void test() {
        DemoTask.doTask("LRU Basic", this::basic);
    }

    private void basic() {
        LRUMap<Integer, String> map = new LRUMap<>(3);
        IntStream.range(1, 4).forEach(i -> {
            map.put(i, String.valueOf(i));
        });

        SimpleLogger.println("Basic map : " + map);

        assertTrue(map.size() == 3);
        map.put(4, "4");
        SimpleLogger.println("After put 4 : " + map);

        assertTrue(map.size() == 3);
        assertNull(map.get(1));
    }
}

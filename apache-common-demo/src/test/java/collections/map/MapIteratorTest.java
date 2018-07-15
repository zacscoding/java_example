package collections.map;

import org.apache.commons.collections4.IterableMap;
import org.apache.commons.collections4.MapIterator;
import org.apache.commons.collections4.map.FixedSizeMap;
import org.apache.commons.collections4.map.HashedMap;
import org.junit.Test;
import util.DemoTask;
import util.SimpleLogger;

/**
 * ref : https://www.tutorialspoint.com/commons_collections/commons_collections_mapiterator.htm
 */
public class MapIteratorTest {

    @Test
    public void test() {
        DemoTask.doTask("MapIterator", this::iterate);
    }

    private void iterate() {
        IterableMap<String, String> map = new HashedMap<>();

        map.put("1", "Zac");
        map.put("2", "Codding");
        map.put("3", "Test");

        MapIterator<String, String> iterator = map.mapIterator();
        while (iterator.hasNext()) {
            Object key = iterator.next();
            Object value = iterator.getValue();

            SimpleLogger.println("Key : {} | Value : {}", key, value);

            iterator.setValue(value + "_AddedValue");
        }

        System.out.println(map);
    }

}

package apahce.collections.bidimap;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;

import org.apache.commons.collections4.BidiMap;
import org.apache.commons.collections4.OrderedBidiMap;
import org.apache.commons.collections4.bidimap.TreeBidiMap;
import org.junit.Test;
import util.DemoTask;
import util.SimpleLogger;

/**
 * ref :: https://www.tutorialspoint.com/commons_collections/commons_collections_bag.htm
 */
public class BidiMapDemoTest {

    @Test
    public void test() {
        DemoTask.doTask("basic", this::basic);
        DemoTask.doTask("iterate", this::iterate);
    }

    private void basic() {
        BidiMap<String, String> bidi = new TreeBidiMap<>();

        bidi.put("one", "1");
        bidi.put("two", "2");
        bidi.put("three", "3");

        assertThat(bidi.get("one"), is("1"));
        assertThat(bidi.get("two"), is("2"));
        assertThat(bidi.get("three"), is("3"));

        assertThat(bidi.getKey("1"), is("one"));
        assertThat(bidi.getKey("2"), is("two"));
        assertThat(bidi.getKey("3"), is("three"));

        bidi.removeValue("3");
        assertNull(bidi.get("three"));

        SimpleLogger.println("bidi origin : " + bidi.toString());
        SimpleLogger.println("bidi inverse : " + bidi.inverseBidiMap().toString());
    }

    private void iterate() {
        TreeBidiMap<String, String> bidi = new TreeBidiMap<>();

        bidi.put("one", "1");
        bidi.put("two", "2");
        bidi.put("three", "3");

        String key = bidi.firstKey();

        System.out.println("Origin tree.. : " + bidi.toString());
        while (true) {
            SimpleLogger.println("key : {} | value : {}", key, bidi.get(key));
            key = bidi.nextKey(key);
            if (key == null) {
                break;
            }
        }

        System.out.println("---------------------------------------------------------");

        OrderedBidiMap inverseMap = bidi.inverseBidiMap();
        System.out.println("Inverse tree.. : " + inverseMap);

        key = (String) inverseMap.firstKey();
        while (true) {
            SimpleLogger.println("key : {} | value : {}", key, inverseMap.get(key));
            key = (String) inverseMap.nextKey(key);
            if (key == null) {
                break;
            }
        }

    }
}
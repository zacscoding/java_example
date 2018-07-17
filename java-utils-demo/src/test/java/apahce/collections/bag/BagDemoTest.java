package apahce.collections.bag;

import static org.junit.Assert.assertTrue;

import org.apache.commons.collections4.Bag;
import org.apache.commons.collections4.bag.HashBag;
import org.junit.Test;
import util.DemoTask;
import util.SimpleLogger;

/**
 * ref :: https://www.tutorialspoint.com/commons_collections/commons_collections_bag.htm
 */
public class BagDemoTest {

    @Test
    public void test() {
        DemoTask.doTask("basic", this::basic);
    }

    private void basic() {
        Bag<String> bag = new HashBag<>();

        bag.add("a");
        bag.add("b", 2);
        bag.add("c", 3);
        bag.add("e");
        SimpleLogger.println("bag.toSring() : " + bag.toString());

        assertTrue(bag.getCount("a") == 1);
        assertTrue(bag.getCount("b") == 2);
        assertTrue(bag.getCount("c") == 3);
        assertTrue(bag.getCount("e") == 1);

        assertTrue(bag.uniqueSet().size() == 4);
    }
}

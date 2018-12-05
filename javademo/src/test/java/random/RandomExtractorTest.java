package random;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.stream.IntStream;
import org.junit.Test;
import random.RandomExtractor.NoRemainItemException;

/**
 * @author zacconding
 * @Date 2018-12-05
 * @GitHub : https://github.com/zacscoding
 */
public class RandomExtractorTest {

    @Test
    public void testIntegers() {
        final int start = 10;
        final int last = 20;
        int count = last - start + 1;

        RandomExtractor<Integer> selector = RandomExtractor.createIntegerItems(start, last);
        IntStream.range(0, count).forEach(i -> {
            try {
                int item = selector.nextItem();
                System.out.println("Select : " + item);
                assertTrue(item >= start);
                assertTrue(item <= last);
            } catch (NoRemainItemException e) {
                fail();
            }
        });

        try {
            selector.nextItem();
            fail();
        } catch (NoRemainItemException e) {

        }
    }

    @Test
    public void testStrings() {
        String[] items = {"aa", "bb", "cc", "dd"};
        RandomExtractor<String> selector = new RandomExtractor<>(items);

        for (int i = 0; i < items.length; i++) {
            try {
                String selected = selector.nextItem();
                System.out.println("Select : " + selected);
                assertTrue(contains(items, selected));
            } catch (NoRemainItemException e) {

            }
        }

        try {
            selector.nextItem();
            fail();
        } catch (NoRemainItemException e) {

        }
    }

    private boolean contains(String[] items, String item) {
        for (String i : items) {
            if (i.equals(item)) {
                return true;
            }
        }

        return false;
    }
}

package random;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Random;
import org.junit.Test;
import random.RandomIndexGenerator.NotRemainIndexException;

/**
 * @author zacconding
 * @Date 2018-11-19
 * @GitHub : https://github.com/zacscoding
 */
public class RandomIndexGeneratorTest {

    @Test
    public void testNextIndex() throws Exception {
        int testCase = 5;
        final Random random = new Random();

        while (testCase-- > 0) {
            int start = random.nextInt(5);
            int last = random.nextInt(5) + start;

            if (testCase == 1) {
                start = 0;
                last = 0;
            }

            RandomIndexGenerator generator = new RandomIndexGenerator(start, last);
            for (int i = 0; i < last - start + 1; i++) {
                int randomIndex = generator.nextIndex();
                assertTrue(isRange(start, last, randomIndex));
            }

            assertFalse(generator.isRemain());
        }
    }

    @Test(expected = NotRemainIndexException.class)
    public void testOverCall() {
        RandomIndexGenerator generator = new RandomIndexGenerator(1, 2);
        try {
            assertTrue(isRange(1, 2, generator.nextIndex()));
            assertTrue(isRange(1, 2, generator.nextIndex()));
        } catch (Exception e) {
            fail();
        }

        // throw Exception
        generator.nextIndex();
    }

    private boolean isRange(int start, int last, int index) {
        return (index >= start) && (index <= last);
    }
}

package guava.basicutils;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static org.junit.Assert.fail;

import org.junit.Test;
import util.DemoTask.DoTask;

/**
 * @author zacconding
 * @Date 2018-07-18
 * @GitHub : https://github.com/zacscoding
 */
public class PreconditionTest {

    @Test
    public void basic() {
        testCheckArgs(1);

        expectedFail(() ->testCheckArgs(-1));

        testCheckNotNull("value1");
        expectedFail(() ->testCheckNotNull(null));
    }

    private void testCheckArgs(int i) {
        checkArgument(i >= 0, "Argument was %s but expected nonnegative", i);
        System.out.println("It is positive number : " + i);
    }

    private void testCheckNotNull(String value) {
        checkNotNull(value);
        System.out.println("It is not null : " + value);
    }

    private void expectedFail(DoTask task) {
        try {
            task.doSomething();
            fail();
        } catch(Exception e) {

        }
    }
}
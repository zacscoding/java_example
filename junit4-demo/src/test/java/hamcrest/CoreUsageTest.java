package hamcrest;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.describedAs;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsAnything.anything;
import static org.hamcrest.core.IsEqual.equalTo;

import org.junit.Test;

/**
 * @author zacconding
 * @Date 2018-11-26
 * @GitHub : https://github.com/zacscoding
 */
public class CoreUsageTest {

    @Test
    public void usage_anything() {
        assertThat(1, is(anything()));
        assertThat("aaa", is(anything()));
        assertThat(3.5D, is(anything()));
    }

    @Test
    public void usage_describedAs() {
        String addr = "hivava";
        assertThat(addr, describedAs("expected value : hivava2", is("hivava2")));
        /*
        java.lang.AssertionError:
        Expected: expected value : hivava2
             but: was "hivava"
        Expected :expected value : hivava2
        Actual   :"hivava"
         */
    }

    @Test
    public void usage_equalTo() {
        String addr = "hivava1";
        assertThat(addr, equalTo("hivava1"));
        assertThat(addr, is(equalTo("hivava1")));
    }

}

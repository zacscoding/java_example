package hamcrest;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.sameInstance;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.hamcrest.core.IsNull.nullValue;
import static org.hamcrest.object.HasToString.hasToString;

import org.junit.Test;

/**
 * @author zacconding
 * @Date 2018-11-26
 * @GitHub : https://github.com/zacscoding
 */
public class ObjectUsageTest {

    @Test
    public void usage_hasToString() {
        String address = "hivava";
        assertThat(address, is(hasToString("hivava")));
    }

    @Test
    public void usage_instanceOf() {
        String addr = "hivava";
        assertThat(addr, is(instanceOf(String.class)));
    }

    @Test
    public void usage_nullValue_notNullValue() {
        String addr = null;
        assertThat(addr, is(nullValue()));
        addr = "hivava";
        assertThat(addr, is(notNullValue()));
    }

    @Test
    public void usage_sameInstance() {
        String addr = "hivava";
        String addr2 = "hivava";

        assertThat(addr, is(sameInstance(addr2)));

        String addr3 = new StringBuilder().append("hivava").toString();
        assertThat(addr, not(sameInstance(addr3)));
    }


}

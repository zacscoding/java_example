package hamcrest;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.endsWith;
import static org.hamcrest.Matchers.equalToIgnoringCase;
import static org.hamcrest.Matchers.equalToIgnoringWhiteSpace;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.hasItemInArray;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.lessThan;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.startsWith;
import static org.hamcrest.collection.IsMapContaining.hasEntry;
import static org.hamcrest.collection.IsMapContaining.hasKey;
import static org.hamcrest.collection.IsMapContaining.hasValue;
import static org.hamcrest.core.AllOf.allOf;
import static org.hamcrest.core.AnyOf.anyOf;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsCollectionContaining.hasItem;
import static org.hamcrest.core.IsCollectionContaining.hasItems;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.number.IsCloseTo.closeTo;
import static org.hamcrest.number.OrderingComparison.lessThanOrEqualTo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.Test;

/**
 * @author zacconding
 * @Date 2018-11-26
 * @GitHub : https://github.com/zacscoding
 */
public class OthersUsageTest {

    // tag : Logical
    @Test
    public void usage_allOf() {
        String addr = "hivava";
        assertThat(addr, allOf(is("hivava"), equalTo("hivava")));
    }

    @Test
    public void usage_anyOf() {
        String addr = "hivava";
        assertThat(addr, anyOf(not("hivava"), is("hivava2"), equalTo("hivava")));
    }

    @Test
    public void usage_not() {
        String addr = "hivava";
        assertThat(addr, not("hivava2"));
    }
    // -- tag : Logical

    // tag : Beans
    @Test
    public void usage_hasProperty() {
        TestForHasProperty bean = new TestForHasProperty();
        assertThat(bean, hasProperty("someProperty"));
    }

    private static class TestForHasProperty {

        private String someProperty;

        public void setSomeProperty(String someProperty) {
            this.someProperty = someProperty;
        }
    }
    // -- tag : Beans

    // tag : Collection
    @Test
    public void test_array() {
        String[] arr = new String[] {"hivava1"};
        String[] arr2 = new String[] {"hivava2"};
    }

    @Test
    public void usage_hasXXX() {
        Map<String, String> map = new HashMap<>();
        map.put("key1", "hivava1");
        map.put("key2", "hivava2");

        assertThat(map, is(hasEntry("key1", "hivava1")));
        assertThat(map, is(hasEntry("key2", "hivava2")));
        assertThat(map, not(hasEntry("key1", "hivava3")));

        assertThat(map, is(hasKey("key1")));
        assertThat(map, is(hasKey("key2")));

        assertThat(map, is(hasValue("hivava1")));
        assertThat(map, is(hasValue("hivava2")));
        assertThat(map, not(hasValue("hivava3")));
    }

    @Test
    public void usage_hasItem() {
        List<String> list = new ArrayList<>();
        list.add("key1");
        list.add("key2");

        assertThat(list, is(hasItem("key1")));
        assertThat(list, is(hasItems("key1", "key2")));
        assertThat(list, not(hasItem("key3")));
    }

    @Test
    public void usage_hasItemInArray() {
        String[] arr = new String[] {"hivava1", "hivava2", "hivava3"};

        assertThat(arr, is(hasItemInArray("hivava1")));
        assertThat(arr, is(hasItemInArray("hivava2")));
        assertThat(arr, is(hasItemInArray("hivava3")));
        assertThat(arr, not(hasItemInArray("hivava4")));
    }
    // -- tag : Collection

    // tag : Number
    @Test
    public void usage_closeTo() {
        double value = 2.56D;
        assertThat(value, is(closeTo(2.55D, 0.03D)));
        assertThat(value, is(closeTo(2.54D, 0.03D)));
    }

    @Test
    public void usage_greaterThan() {
        assertThat(2.3F, is(greaterThan(2.299999F)));
        assertThat(2.299999F, is(greaterThanOrEqualTo(2.299999F)));
    }

    @Test
    public void usage_lessThan() {
        assertThat(2.299998F, is(lessThan(2.299999F)));
        assertThat(2.299999F, is(lessThanOrEqualTo(2.299999F)));
    }
    // -- tag : Number

    // tag : Text
    @Test
    public void usage_containsString() {
        String addr = "aabbccdd";
        assertThat(addr, is(containsString("aabb")));
        assertThat(addr, is(containsString("aabbcc")));
        assertThat(addr, is(containsString("cd")));
    }

    @Test
    public void usage_startsWith() {
        String addr = "aabbccdd";
        assertThat(addr, is(startsWith("a")));
        assertThat(addr, is(startsWith("aa")));
        assertThat(addr, is(startsWith("aabbc")));
    }

    @Test
    public void usage_endsWith() {
        String addr = "aabbccdd";
        assertThat(addr, is(endsWith("d")));
        assertThat(addr, is(endsWith("dd")));
        assertThat(addr, is(endsWith("cdd")));
    }

    @Test
    public void usage_equalToIgnoringCase() {
        String addr = "aAbBcCdD";
        assertThat(addr, is(equalToIgnoringCase("AaBbCcDd")));
        assertThat(addr, is(equalToIgnoringCase("aabbccdd")));
    }

    @Test
    public void usage_equalToIgnoringWhiteSpace() {
        String addr = "abc def ghi";
        assertThat(addr, is(equalToIgnoringWhiteSpace("abc def ghi  ")));
        assertThat(addr, is(equalToIgnoringWhiteSpace("  abc def ghi  ")));
    }

    // -- tag : Text
}

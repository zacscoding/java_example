package guava.basicutils;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import com.google.common.base.Optional;
import com.google.common.base.Strings;
import org.junit.Test;

/**
 * https://github.com/google/guava/wiki/UsingAndAvoidingNullExplained
 *
 * @author zacconding
 * @Date 2018-07-18
 * @GitHub : https://github.com/zacscoding
 */
public class UsingAvoidingNullTest {
    @Test
    public void test() {
        Optional<String> possible = Optional.of("test");
        assertTrue(possible.isPresent());
        assertThat(possible.get(), is("test"));
    }

    @Test
    public void test2() {
        String result = Strings.emptyToNull("");
        assertNull(result);
        assertTrue(Strings.isNullOrEmpty(result));
        assertThat(Strings.nullToEmpty(result), is(""));
    }
}

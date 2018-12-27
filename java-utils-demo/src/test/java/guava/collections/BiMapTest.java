package guava.collections;

import static org.junit.Assert.fail;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import java.util.Map;
import org.junit.Test;

/**
 * @author zacconding
 * @Date 2018-10-23
 * @GitHub : https://github.com/zacscoding
 */
public class BiMapTest {

    @Test
    public void defaultBiMap() {
        BiMap<String, Integer> users = HashBiMap.create();

        users.values();
        users.put("Bob", 42);
        System.out.println("users.inverse().get(42) : " + users.inverse().get(42));

        try {
            users.put("Bob2", 42);
            System.out.println("users.inverse().get(42) : " + users.inverse().get(42));
            fail();
        } catch(IllegalArgumentException e) {
        }
    }

    @Test
    public void defaultMultiMap() {
        Map<String, String> map;

        Multimap<String ,Integer> multi = HashMultimap.create();
        multi.put("Bob", 42);
        multi.put("Bob", 43);

        System.out.println(multi.asMap().get("Bob").size());
        System.out.println(multi.containsValue(42));
    }
}

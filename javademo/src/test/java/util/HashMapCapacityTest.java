package util;

import java.util.HashMap;
import java.util.Map;
import org.junit.Test;

/**
 * @author zacconding
 * @Date 2018-11-21
 * @GitHub : https://github.com/zacscoding
 */
public class HashMapCapacityTest {

    @Test
    public void tryToPut() {
        Map<Integer, Integer> map = new HashMap<>(10);
        for (int i = 1; i < 20; i++) {
            System.out.println("## before put " + i);
            map.put(i, i);
        }
    }
}

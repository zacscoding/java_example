package util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author zacconding
 * @Date 2018-03-09
 * @GitHub : https://github.com/zacscoding
 */
public class ArrayListTest {


    @Test
    public void sizeTest() throws Exception {
        ArrayList<String> list = new ArrayList<>();

        for (Field field : list.getClass().getDeclaredFields()) {
            if (field.getName().equals("size")) {
                field.setAccessible(true);
                System.out.println(field.get(list));
                break;
            }
        }
    }

    @Test
    public void copyOf() {
        String[] strings = new String[]{"test1"};
        Assert.assertTrue(strings.length == 1);
        strings = Arrays.copyOf(strings, 2);
        Assert.assertTrue(strings.length == 2);
    }

    @Test
    public void add() {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            list.add("str" + i);
        }
    }

    @Test
    public void growSize() {
        int next = 2;
        for (int i = 10; i < 20; i++) {
            SimpleLogger.print("{} => ", next);
            next = (next) + (next >> 1);
            SimpleLogger.print("{} || ", next);
        }
    }


}

package stream;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;
import org.junit.Before;
import org.junit.Test;
import util.SimpleLogger;

/**
 * @author zacconding
 * @Date 2018-02-06
 * @GitHub : https://github.com/zacscoding
 */
public class StreamDefault {

    List<String> defaultList;

    @Before
    public void setUp() {
        defaultList = Arrays.asList("value1", "value2", "value3");
    }

    @Test
    public void iterate() {
        Iterator<String> itr = defaultList.iterator();
        while (itr.hasNext()) {
            System.out.println(itr.next());
        }

        System.out.println("=========================================");

        Stream<String> stream = defaultList.stream();
        stream.forEach(name -> {
            System.out.println(name);
        });
    }

    @Test
    public void parallelStream() {
        Stream<String> parallelStream = defaultList.parallelStream();
        parallelStream.forEach(name -> {
            SimpleLogger.println("value : {}, current thread name : {}", name, Thread.currentThread().getName());
        });
    }

}

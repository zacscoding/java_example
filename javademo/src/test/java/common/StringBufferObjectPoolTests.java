package common;

import java.io.FileReader;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import pool.basic.SringBufferObjectPool;
import pool.basic.StringBufferFactory;
import util.SimpleLogger;

/**
 * @author zacconding
 * @Date 2018-12-27
 * @GitHub : https://github.com/zacscoding
 */
public class StringBufferObjectPoolTests {

    @Test
    public void pooling() throws Exception {
        SringBufferObjectPool pool = new SringBufferObjectPool(new GenericObjectPool<>(new StringBufferFactory()));

        for (int i = 0; i < 5; i++) {
            SimpleLogger.println(">> Start use pool : {}", i);
            pool.readToString(new FileReader(new ClassPathResource("common/file1").getFile()));
            SimpleLogger.println("------------------------------------");
        }
    }
}

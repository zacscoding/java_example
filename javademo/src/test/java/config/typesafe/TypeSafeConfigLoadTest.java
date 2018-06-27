package config.typesafe;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.junit.Test;
import util.SimpleLogger;

/**
 * @author zacconding
 * @Date 2018-06-14
 * @GitHub : https://github.com/zacscoding
 */
public class TypeSafeConfigLoadTest {

    @Test
    public void foo() {
        Config config1 = ConfigFactory.parseResources("config-test1.conf");
        Config config2 = ConfigFactory.parseResources("config-test2.conf");

        SimpleLogger.build()
                    .appendln("config1.getInt(\"foo.a\") : " + config1.getInt("foo.a"))
                    .appendln("config2.getInt(\"foo.b\") : " + config2.getInt("foo.b"))
                    .flush();

        System.out.println("=======================================================================");

        Config resolver = config1.withFallback(config2).resolve();
        SimpleLogger.build()
                    .appendln("resolver.getInt(\"foo.a\") : " + resolver.getInt("foo.a"))
                    .appendln("resolver.getInt(\"foo.b\") : " + resolver.getInt("foo.b"))
                    .flush();
    }
}

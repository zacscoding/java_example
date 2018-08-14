package config.property;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import java.util.Properties;
import org.junit.Before;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import util.SimpleLogger;

/**
 * @author zacconding
 * @Date 2018-08-14
 * @GitHub : https://github.com/zacscoding
 */
public class PropertiesOverrideTest {

    String[] keys;
    boolean displayValue = true;

    @Before
    public void setUp() {
        keys = new String[] {"server.port", "server.test", "server.context"};
    }

    @Test
    public void test() {
        SimpleLogger logger = SimpleLogger.build();
        try {
            String defaultPath = "property/sample1.properties";
            String overridePath = "property/sample2.properties";

            Properties properties = new Properties();
            properties.load(new ClassPathResource(defaultPath).getInputStream());

            assertThat(properties.getProperty("server.port"), is("8080"));
            assertThat(properties.getProperty("server.context"), is("/test"));

            if (displayValue) {
                logger.appendln("> First load : {}", defaultPath);
                for (String key : keys) {
                    logger.appendln(">> Key : {} | Value : {}", key, properties.getProperty(key));
                }
                logger.appendln("\n\n");
            }

            properties.load(new ClassPathResource(overridePath).getInputStream());
            if (displayValue) {
                logger.appendln("> Second load : {}", overridePath);
                for (String key : keys) {
                    logger.appendln(">> Key : {} | Value : {}", key, properties.getProperty(key));
                }
            }

            assertThat(properties.getProperty("server.port"), is("8080"));
            assertThat(properties.getProperty("server.context"), is("/new-test"));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            logger.flush();
        }
    }
}

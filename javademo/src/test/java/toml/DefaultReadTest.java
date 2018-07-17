package toml;

import com.moandjiezana.toml.Toml;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import util.SimpleLogger;

/**
 * @author zacconding
 * @Date 2018-07-10
 * @GitHub : https://github.com/zacscoding
 */
public class DefaultReadTest {

    @Test
    public void defaultRead() throws Exception {
        Toml toml = new Toml();
        toml.read(new ClassPathResource("toml/default.toml").getInputStream());

        SimpleLogger.println("name : {}, hobby : {}", toml.getString("name"), toml.getString("hobby"));
    }
}

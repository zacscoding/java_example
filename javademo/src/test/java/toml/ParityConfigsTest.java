package toml;

import com.moandjiezana.toml.Toml;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import util.GsonUtil;

/**
 * @author zacconding
 * @Date 2018-07-10
 * @GitHub : https://github.com/zacscoding
 */
public class ParityConfigsTest {

    @Test
    public void readTest() throws Exception {
        TomlConfigs config = new Toml().read(new ClassPathResource("toml/parity.toml").getInputStream()).to(TomlConfigs.class);
        GsonUtil.printGsonPretty(config);
    }
}

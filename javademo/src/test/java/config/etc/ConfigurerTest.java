package config.etc;

import config.Configurer;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import org.junit.Before;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;

/**
 * @author zacconding
 * @Date 2019-01-18
 * @GitHub : https://github.com/zacscoding
 */
public class ConfigurerTest {

    @Before
    public void setUp() throws Exception {
        System.setProperty("agent_config_location",
            "E:\\test\\configurer.json"
        );
    }

    @Test
    public void detectChanged() throws Exception {
        CountDownLatch countDownLatch = new CountDownLatch(2);

        String serviceName = "";
        while (!Thread.currentThread().isInterrupted()) {
            if (Configurer.error()) {
                System.out.println("Configurer error!!");
                return;
            }

            if (!serviceName.equals(Configurer.getInstance().getServiceName())) {
                serviceName = Configurer.getInstance().getServiceName();
                System.out.println("Changed service name :: " + serviceName);
                countDownLatch.countDown();
            }

            if (countDownLatch.getCount() == 0L) {
                break;
            }

            TimeUnit.SECONDS.sleep(1L);
        }
    }
}

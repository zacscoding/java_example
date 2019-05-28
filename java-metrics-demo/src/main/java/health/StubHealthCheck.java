package health;

import com.codahale.metrics.health.HealthCheck;
import java.util.Random;
import lombok.extern.slf4j.Slf4j;

/**
 * @GitHub : https://github.com/zacscoding
 */
@Slf4j
public class StubHealthCheck extends HealthCheck {

    public static StubHealthCheck newInstance(String name) {
        return new StubHealthCheck(name);
    }

    private String name;

    private StubHealthCheck(String name) {
        this.name = name;
    }

    @Override
    protected Result check() throws Exception {
        // 10% fail
        int rand = new Random().nextInt(10);
        boolean isHealth = rand < 1;

        logger.info("[StubHealthCheck] Health check {} -> {}", name, isHealth ? "Healthy" : "Unhealthy");

        if (!isHealth) {
            return HealthCheck.Result.unhealthy("Cannot connect to " + name);
        }

        return HealthCheck.Result.healthy("Connected to " + name);
    }
}

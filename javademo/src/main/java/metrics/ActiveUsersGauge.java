package metrics;

import com.codahale.metrics.CachedGauge;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * http://www.baeldung.com/dropwizard-metrics
 */
public class ActiveUsersGauge extends CachedGauge<List<Long>> {

    public ActiveUsersGauge(long timeout, TimeUnit timeoutUnit) {
        super(timeout, timeoutUnit);
    }

    @Override
    protected List<Long> loadValue() {
        System.out.println("load Value is called..");
        return getActiveUserCount();
    }

    private List<Long> getActiveUserCount() {
        return Arrays.asList(12L);
    }
}

package metrics;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import com.codahale.metrics.ConsoleReporter;
import com.codahale.metrics.Meter;
import com.codahale.metrics.MetricRegistry;
import java.util.concurrent.TimeUnit;
import org.testng.annotations.Test;

/**
 * @author zacconding
 * @Date 2018-11-27
 * @GitHub : https://github.com/zacscoding
 */
public class MetricsGettingStartTest {

    @Test
    public void startReportAndMark() throws InterruptedException {
        MetricRegistry metrics = new MetricRegistry();
        ConsoleReporter reporter = ConsoleReporter.forRegistry(metrics).convertRatesTo(TimeUnit.SECONDS).convertDurationsTo(TimeUnit.MILLISECONDS).build();
        reporter.start(1, TimeUnit.SECONDS);

        Meter requests = metrics.meter("requests");
        requests.mark();
        TimeUnit.SECONDS.sleep(5L);
        /*
        18. 11. 27 오후 1:29:37 ==========================================================

        -- Meters ----------------------------------------------------------------------
        requests
                     count = 1
                 mean rate = 1.00 events/second
             1-minute rate = 0.00 events/second
             5-minute rate = 0.00 events/second
            15-minute rate = 0.00 events/second


        18. 11. 27 오후 1:29:38 ==========================================================

        -- Meters ----------------------------------------------------------------------
        requests
                     count = 1
                 mean rate = 0.50 events/second
             1-minute rate = 0.00 events/second
             5-minute rate = 0.00 events/second
            15-minute rate = 0.00 events/second
    */
    }

    @Test
    public void usage_mark() {
        MetricRegistry metrics = new MetricRegistry();

        Meter meter1 = metrics.meter("request1");
        Meter meter2 = metrics.meter("request2");

        MetersTestClass inst1 = new MetersTestClass(meter1);
        MetersTestClass inst2 = new MetersTestClass(meter2);

        inst1.add(1, 2);
        inst1.add(1, 2);
        inst1.add(1, 2);

        inst2.add(1, 2);

        assertTrue(meter1.getCount() == 3L);
        assertTrue(meter2.getCount() == 1L);
    }


    private static class MetersTestClass {

        private final Meter requests;

        public MetersTestClass(Meter requests) {
            this.requests = requests;
        }

        public int add(int num1, int num2) {
            requests.mark();
            return num1 + num2;
        }
    }


}

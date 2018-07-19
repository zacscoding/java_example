package metrics;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.codahale.metrics.Counter;
import com.codahale.metrics.Gauge;
import com.codahale.metrics.Meter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;
import org.junit.Test;
import util.DemoTask;
import util.SimpleLogger;

/**
 * http://www.baeldung.com/dropwizard-metrics
 */
public class MetricRegistryTest {


    @Test
    public void basic() {
        DemoTask.doTask("Meter", this::meter);
        DemoTask.doTask("Gauge", this::guage);
        DemoTask.doTask("Counter", this::counter);
    }

    private void meter() {
        Meter meter = new Meter();
        long initCount = meter.getCount();
        assertTrue(initCount == 0L);
        SimpleLogger.println("Init count : {}", initCount);

        meter.mark(); // event occurrences count
        assertTrue(meter.getCount() == 1L);
        SimpleLogger.println("After mark() : {}", meter.getCount());

        meter.mark(20);
        assertTrue(meter.getCount() == 21L);
        SimpleLogger.println("After mark(20) : {}", meter.getCount());

        SimpleLogger
            .println("getMeanRate() : {} | getOneMinuteRate() : {} | getFiveMinuteRate() : {} | getFifteenMinuteRate() : {}", meter.getMeanRate(), meter.getOneMinuteRate(), meter.getFiveMinuteRate(),
                meter.getFifteenMinuteRate());
    }

    private void guage() {
        AttendanceRatioGauge attendanceRatioGauge = new AttendanceRatioGauge(15, 20);
        assertTrue(attendanceRatioGauge.getValue() == 0.75D);

        Gauge<List<Long>> activeUserGauge = new ActiveUsersGauge(1, TimeUnit.SECONDS);
        List<Long> expected = Arrays.asList(12L);
        assertEquals(expected, activeUserGauge.getValue());
        System.out.println("recall... " + activeUserGauge.getValue());
        System.out.println("recall... " + activeUserGauge.getValue());
        System.out.println("recall... " + activeUserGauge.getValue());

        try {
            Thread.sleep(1200L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("recall... after 5 sec" + activeUserGauge.getValue());
    }

    private void counter() throws Exception{
        Counter counter = new Counter();
        long initCount = counter.getCount();
        assertTrue(counter.getCount() == 0L);
        SimpleLogger.println("init count : {}", initCount);

        counter.inc();
        assertTrue(counter.getCount() == 1L);
        SimpleLogger.println("after inc() : {}", counter.getCount());

        counter.inc(3);
        assertTrue(counter.getCount() == 4L);
        SimpleLogger.println("after inc(3) : {}", counter.getCount());

        counter.dec();
        assertTrue(counter.getCount() == 3L);
        SimpleLogger.println("after dec() : {}", counter.getCount());

        final Counter counter2 = new Counter();

        List<Thread> threads = new ArrayList<>();
        IntStream.range(0, 100).forEach(i->{
            Thread t = new Thread(() -> counter2.inc());
            threads.add(t);
            t.start();
        });

        for(Thread t : threads) {
            t.join();
        }

        assertTrue(counter2.getCount() == 100L);
    }
}

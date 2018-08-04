package metrics;

import com.codahale.metrics.ConsoleReporter;
import com.codahale.metrics.Meter;
import com.codahale.metrics.MetricRegistry;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import org.junit.Test;

/**
 * @author zacconding
 * @Date 2018-07-28
 * @GitHub : https://github.com/zacscoding
 */
public class Temp {

    static final MetricRegistry metrics = new MetricRegistry();

    public static void main(String args[]) {
        startReport();

        Thread t= new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                int random = new Random().nextInt(1000);
                if (random <= 10) {
                    Meter requests = metrics.meter("requests");
                    requests.mark();
                }
            }
        });

        t.setDaemon(true);
        t.start();
        wait1Minute();
    }

    static void startReport() {
        ConsoleReporter reporter = ConsoleReporter.forRegistry(metrics)
                                                  .convertRatesTo(TimeUnit.SECONDS)
                                                  .convertDurationsTo(TimeUnit.MILLISECONDS)
                                                  .build();
        reporter.start(1, TimeUnit.SECONDS);
    }

    static void wait1Minute() {
        try {
            TimeUnit.MINUTES.sleep(1);
        }
        catch(InterruptedException e) {}
    }


}

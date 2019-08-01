package report;

import com.codahale.metrics.CsvReporter;
import com.codahale.metrics.Meter;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Slf4jReporter;
import java.io.File;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 */
public class ReporterMain {

    private static final Logger logger = LoggerFactory.getLogger(ReporterMain.class);

    static final MetricRegistry metrics = new MetricRegistry();
    static final Meter requests = metrics.meter("requests");

    public static void main(String[] args) throws Exception {
        startSlf4jReport();
        startCsvReport();

        Thread markThread = new Thread(() -> {
            try {
                while (!Thread.currentThread().isInterrupted()) {
                    requests.mark();
                    TimeUnit.SECONDS.sleep(2L);
                }
            } catch (Exception e) {
                // ignore
            }
        });
        markThread.setDaemon(true);
        markThread.start();

        TimeUnit.MINUTES.sleep(1L);
    }

    static void startSlf4jReport() {
        logger.info("## Start slf4j report");
        final Slf4jReporter reporter = Slf4jReporter.forRegistry(metrics)
            .outputTo(LoggerFactory.getLogger("[REPORTER]"))
            .convertRatesTo(TimeUnit.SECONDS)
            .convertDurationsTo(TimeUnit.MILLISECONDS)
            .build();

        reporter.start(1, TimeUnit.SECONDS); // display every 1 min
        logger.info("Success to start");
    }

    static void startCsvReport() {
        logger.info("## Start csv report");

        final CsvReporter reporter = CsvReporter.forRegistry(metrics)
            .formatFor(Locale.KOREA)
            .convertRatesTo(TimeUnit.SECONDS)
            .convertDurationsTo(TimeUnit.MILLISECONDS)
            .build(new File("./logs/"));

        reporter.start(1, TimeUnit.SECONDS);
    }
}

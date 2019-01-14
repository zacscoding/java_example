import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.stream.LongStream;
import lombok.extern.slf4j.Slf4j;
import metrics1.DoubleSupplier;
import metrics1.JvmMemoryMetrics1;
import metrics1.MetricRegistry1;
import metrics1.MetricSet1;
import metrics1.SystemMetrics1;

/**
 * @author zacconding
 * @Date 2019-01-14
 * @GitHub : https://github.com/zacscoding
 */
@Slf4j
public class Main {

    public static void main(String[] args) throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        final SystemMetrics1 systemMetrics1 = new SystemMetrics1();
        final JvmMemoryMetrics1 jvmMemoryMetrics1 = new JvmMemoryMetrics1();
        Thread t = new Thread(() -> {
            try {
                while (!Thread.currentThread().isInterrupted()) {
                    MetricRegistry1 registry1 = new MetricRegistry1();
                    //systemMetrics1.start(registry1);
                    jvmMemoryMetrics1.start(registry1);

                    registry1.getMetricSets().entrySet().forEach(entry -> {
                        MetricSet1 set = entry.getValue();
                        Map<String, DoubleSupplier> samples = set.getSamples();
                        System.out.println("--------------------------------------");
                        samples.entrySet().forEach(sampleEntry -> {
                            log.info("{} >> {}", sampleEntry.getKey(), new BigDecimal(sampleEntry.getValue().get()).toPlainString());
                        });
//                        double memoryFree = samples.get("system.memory.actual.free").get();
//                        double memoryTotal = samples.get("system.memory.total").get();
//                        double memoryUsage = 1.0D - (memoryFree / memoryTotal);
//                        log.info("memory usage : {}", new BigDecimal(memoryUsage).toPlainString());
                        System.out.println("--------------------------------------");
                    });
                    TimeUnit.SECONDS.sleep(5L);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } catch (Exception e) {
                log.error("Exception occur", e);
            }
        });
        t.setDaemon(true);
        t.start();

        Thread useMemory = new Thread(() -> {
            try {
                for (int i = 0; i < 2; i++) {
                    log.info("## use memory");
                    Main.useMemory(100000);
                    TimeUnit.SECONDS.sleep(5L);
                }

                log.info("## free memory");

                Main.freeMemory();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } catch (Exception e) {
                log.error("Exception occur", e);
            }
        });
        useMemory.setDaemon(true);
        useMemory.start();

        countDownLatch.await();
    }

    static List<String> uuids;

    static void useMemory(long repeat) {
        if (uuids == null) {
            uuids = new ArrayList<>();
        }

        LongStream.range(0L, repeat).forEach(i -> {
            uuids.add(UUID.randomUUID().toString());
        });
    }

    static void freeMemory() {
        uuids = null;
        //System.runFinalization();
        System.gc();
        System.runFinalization();
    }
}

package timer;

import java.text.SimpleDateFormat;
import java.util.concurrent.TimeUnit;
import org.junit.Test;
import util.SimpleLogger;

/**
 *
 */
public class BatchTimerTets {

    @Test
    public void test() throws Exception {
        SimpleLogger.info("## Start tests..");
        BatchTimer batchTimer = new BatchTimer(1L, 5, 10000, TimeUnit.MILLISECONDS);

        StubProducer p1 = new StubProducer(batchTimer, 5000L, "AA", "node1");
        StubProducer p2 = new StubProducer(batchTimer, 1000L, "AA", "node2");
        StubProducer p3 = new StubProducer(batchTimer, 2200L, "AA", "node3");
        StubProducer p4 = new StubProducer(batchTimer, 3300L, "BB", "node4");
        StubProducer p5 = new StubProducer(batchTimer, 20000L, "BB", "node5");

        StubProducer[] ps = new StubProducer[]{
            p1, p2, p3, p4, p5
        };

        for (StubProducer p : ps) {
            Thread t = new Thread(p);
            t.setDaemon(true);
            t.start();
        }

        SimpleLogger.info("## End tests..");
        TimeUnit.SECONDS.sleep(30);
    }

    public static class StubProducer implements Runnable {

        private BatchTimer batchTimer;
        private long initDelay;
        private String hash;
        private String nodeName;

        public StubProducer(BatchTimer batchTimer, long initDelay, String hash, String nodeName) {
            this.batchTimer = batchTimer;
            this.initDelay = initDelay;
            this.hash = hash;
            this.nodeName = nodeName;
        }

        @Override
        public void run() {
            try {
                Thread.sleep(initDelay);
            } catch (Exception e) {

            }

            batchTimer.newBlock(hash, nodeName);
        }
    }

}

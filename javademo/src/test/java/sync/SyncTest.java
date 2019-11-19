package sync;

import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.junit.Test;

/**
 *
 * @GitHub : https://github.com/zacscoding
 */
public class SyncTest {

    @Test
    public void runTests() throws Exception {
        final Sync sync = new Sync();
        sync.start();

        Thread t = new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    long sleep = new Random().nextInt(10);
                    TimeUnit.SECONDS.sleep(sleep);
                    sync.newPeer();
                } catch (InterruptedException e) {
                    return;
                }
            }
        });
        t.setDaemon(true);
        t.start();

        TimeUnit.MINUTES.sleep(1L);
    }
}

package thread.reentrant;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import org.junit.Test;

/**
 * @author zacconding
 * @Date 2018-05-15
 * @GitHub : https://github.com/zacscoding
 */
public class MultipleWriteTest {

    private int unsafeCount = 0;
    private int safeCount = 0;
    private ReentrantReadWriteLock lock = new ReentrantReadWriteLock(false);

    @Test
    public void writeWithMultipleThreads() throws Exception {
        int threadCnt = 100000;
        Thread[] unsafeThreads = new Thread[threadCnt];
        Thread[] safeThreads = new Thread[threadCnt];
        for (int i = 0; i < threadCnt; i++) {
            unsafeThreads[i] = new Thread(createUnsafe());
            unsafeThreads[i].setDaemon(true);
            safeThreads[i] = new Thread(createSafe());
            safeThreads[i].setDaemon(true);

            unsafeThreads[i].start();
            safeThreads[i].start();
        }

        for (int i = 0; i < threadCnt; i++) {
            unsafeThreads[i].join();
            safeThreads[i].join();
        }

        assertTrue(safeCount == threadCnt);
        assertFalse(unsafeCount == threadCnt);
    }

    private Runnable createUnsafe() {
        return () -> {
            this.unsafeCount++;
        };
    }

    private Runnable createSafe() {
        return () -> {
            try {
                lock.writeLock().lock();
                this.safeCount++;
            } finally {
                lock.writeLock().unlock();
            }
        };
    }
}

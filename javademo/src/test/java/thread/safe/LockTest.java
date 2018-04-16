package thread.safe;

import static org.junit.Assert.assertTrue;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.IntStream;
import org.junit.Before;
import org.junit.Test;

/**
 * @author zacconding
 * @Date 2018-04-16
 * @GitHub : https://github.com/zacscoding
 */
public class LockTest {
    private int threadCount = 0;
    private Object lock;
    private ReentrantLock reentrantLock;
    @Before
    public void setUp() {
        lock = new Object();
        reentrantLock = new ReentrantLock();
    }

    @Test
    public void threadSafeWithLockObject() {
        final Runnable runnable = () -> {
            synchronized (lock) {
                threadCount++;
            }
        };
        assertTrue(1000 == incCnt(1000, runnable));
    }

    @Test
    public void threadSafeWithReentrantLock() {
        final Runnable runnable = () -> {
            reentrantLock.lock();
            threadCount++;
            reentrantLock.unlock();
        };
        assertTrue(1000 == incCnt(1000, runnable));
    }

    @Test
    public void threadSafeWithReentrantLockAndReturn() throws Exception {
        final Runnable runnable = () -> {
            reentrantLock.lock();
            System.out.println("After lock : " + Thread.currentThread().getName());
            if (threadCount > 10) {
                return;
            }
            threadCount++;
            reentrantLock.unlock();
        };

        // not ended
        new Thread(() -> System.out.println(incCnt(1000, runnable))).start();

        for(int i=1; i<11; i++) {
            System.out.println("Wait... " + i);
            TimeUnit.SECONDS.sleep(1L);
        }
    }

    @Test
    public void threadSafeWithReentrantLockAndReturnWithFinally() throws Exception {
        int limit = 100;
        final Runnable runnable = () -> {
            try {
                reentrantLock.lock();
                if (threadCount >= limit) {
                    return;
                }
                threadCount++;
            } finally {
                reentrantLock.unlock();
            }
        };
        assertTrue(limit == incCnt(1000, runnable));
    }

    private int incCnt(int threadCnt, final Runnable runnable) {
        threadCount = 0;
        Thread[] threads = new Thread[threadCnt];

        IntStream.range(0, threadCnt).forEach(i -> {
            threads[i] = new Thread(runnable);
            threads[i].start();
        });

        IntStream.range(0, threadCnt).forEach(i->{
            try {
                threads[i].join();
            } catch(Exception e) {
            }
        });

        return threadCount;
    }
}

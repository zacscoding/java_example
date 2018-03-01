package thread.local;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.stream.IntStream;
import org.junit.Before;
import org.junit.Test;

/**
 * @author zacconding
 * @Date 2018-03-01
 * @GitHub : https://github.com/zacscoding
 */
public class ThreadLocalTest {

    private Runnable runnable;
    private int threadCount = 0;

    @Before
    public void setUp() {
        runnable = () -> {
            // save ThreadLocal
            Thread currentThread = Thread.currentThread();
            ThreadLocalContext ctx = ThreadLocalManager.getOrCreate();
            ctx.setThreadId(currentThread.getId());
            ctx.setThreadName(currentThread.getName());

            synchronized (this) {
                threadCount++;
            }

            // sleep
            try {
                Thread.sleep((long) (Math.random() * 2000L) + 500L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // get ThreadLocal
            ThreadLocalContext ctx2 = ThreadLocalManager.clear();
            assertNotNull(ctx2);
            assertThat(ctx2.getThreadId(), is(currentThread.getId()));
            assertThat(ctx2.getThreadName(), is(currentThread.getName()));
        };
    }

    @Test
    public void test() {
        int size = 1000;
        Thread[] threads = new Thread[size];
        IntStream.range(0, size).forEach(i -> {
            threads[i] = new Thread(runnable);
            threads[i].start();
        });

        IntStream.range(0, size).forEach(i -> {
            try {
                threads[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        assertTrue(threadCount == size);

        System.out.println("Complete");
    }
}

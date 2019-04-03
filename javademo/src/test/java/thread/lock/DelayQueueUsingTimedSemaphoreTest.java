package thread.lock;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;
import org.checkerframework.checker.units.qual.Acceleration;
import org.checkerframework.checker.units.qual.C;
import org.junit.Test;

/**
 * @GitHub : https://github.com/zacscoding
 */
public class DelayQueueUsingTimedSemaphoreTest {

    @Test
    public void testTimedSemaphore() throws Exception {
        final int slots = 10;
        final CountDownLatch countDownLatch = new CountDownLatch(slots);
        ExecutorService executorService = Executors.newFixedThreadPool(slots);
        DelayQueueUsingTimedSemaphore delayQueue = new DelayQueueUsingTimedSemaphore(3, slots);

        IntStream.range(0, slots).forEach(i -> {
            executorService.execute(() -> {
                assertTrue(delayQueue.tryAdd());
                countDownLatch.countDown();
            });
        });
        countDownLatch.await();
        executorService.shutdown();

        System.out.println(delayQueue.availableSlots());
        assertTrue(delayQueue.availableSlots() == 0);
        assertFalse(delayQueue.tryAdd());

        TimeUnit.SECONDS.sleep(4);
        assertTrue(delayQueue.availableSlots() > 0);
    }
}

package thread.lock;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;
import org.junit.Test;

/**
 * @GitHub : https://github.com/zacscoding
 */
public class LoginQueueUsingSemaphoreTest {

    @Test
    public void testSemaphore() {
        final int slotLimit = 5;

        LoginQueueUsingSemaphore semaphore = new LoginQueueUsingSemaphore(slotLimit);
        ExecutorService executorService = Executors.newFixedThreadPool(10);

        IntStream.range(0, 5).forEach(i -> {
            executorService.execute(() -> {
                if (i < slotLimit) {
                    assertTrue(semaphore.tryLogin());
                } else {
                    assertTrue(semaphore.availableSlots() == 0);
                    assertFalse(semaphore.tryLogin());
                    assertTrue(semaphore.hasWaittingUsers());
                }
            });
        });

        executorService.shutdown();
    }
}

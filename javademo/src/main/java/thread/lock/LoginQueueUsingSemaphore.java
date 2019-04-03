package thread.lock;

import java.util.concurrent.Semaphore;
import org.springframework.util.Assert;

/**
 * https://www.baeldung.com/java-semaphore
 */
public class LoginQueueUsingSemaphore {

    private Semaphore semaphore;

    public LoginQueueUsingSemaphore(int slotLimit) {
        Assert.isTrue(slotLimit > 0, "slotLimit must be larger than 0");
        this.semaphore = new Semaphore(slotLimit);
    }

    public boolean tryLogin() {
        return semaphore.tryAcquire();
    }

    public void logout() {
        semaphore.release();
    }

    public int availableSlots() {
        return semaphore.availablePermits();
    }

    public boolean hasWaittingUsers() {
        return semaphore.hasQueuedThreads();
    }
}

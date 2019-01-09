package thread.lock;

import java.util.Objects;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Handle ReentrantReadWriteLocks
 *
 * For example, Suppose we receive sequential numbers with 3 times and want to lock depends on number.
 * we received number 1 and 2 and do task with multiple thread respectively like below.
 *
 * Thread1 : number1 / Thread2 : number1 / Thread3: number1
 * Thread4 : number2 / Thread5 : number2 / Thread6: number2
 *
 * If we lock commonly, Thread2~ Thread6 have to wait completion of Thread1.
 *
 * So depends on task`s number, we can locks multiple threads.
 *
 * @author zacconding
 * @Date 2019-01-09
 * @GitHub : https://github.com/zacscoding
 */
public class LockManager {

    private ReentrantReadWriteLock[] locks;

    public LockManager() {
        this(5);
    }

    public LockManager(int concurrency) {
        if (concurrency < 1) {
            throw new RuntimeException("Concurrency must be lager than 0");
        }

        locks = new ReentrantReadWriteLock[concurrency];
        for (int i = 0; i < concurrency; i++) {
            locks[i] = new ReentrantReadWriteLock();
        }
    }

    public Lock readLock(Object taskIdentifier) {
        return locks[getIndex(taskIdentifier)].readLock();
    }

    public Lock writeLock(Object taskIdentifier) {
        return locks[getIndex(taskIdentifier)].writeLock();
    }

    private int getIndex(Object taskIdentifier) {
        Objects.requireNonNull(taskIdentifier, "taskIdentifier must be not null");
        return Math.abs(taskIdentifier.hashCode() % locks.length);
    }
}

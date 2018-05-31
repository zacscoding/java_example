package thread.reentrant;

import java.util.concurrent.locks.ReentrantReadWriteLock;
import org.junit.Test;

/**
 * https://examples.javacodegeeks.com/core-java/util/concurrent/locks-concurrent/reentrantlock/java-reentrantreadwritelock-example/
 *
 * @author zacconding
 * @Date 2018-05-15
 * @GitHub : https://github.com/zacscoding
 */
public class ReentrantLockTest {

    private static final ReentrantReadWriteLock lock = new ReentrantReadWriteLock(true);

    private static String message = "s";

    @Test
    public void test() throws Exception {
        Thread t1 = new Thread(read());
        Thread t2 = new Thread(concatString("a"));
        Thread t3 = new Thread(concatString("b"));

        t1.setDaemon(true);
        t2.setDaemon(true);
        t3.setDaemon(true);

        t1.start();
        t2.start();
        t3.start();

        t1.join();
        t2.join();
        t3.join();
    }

    private Runnable read() {
        return () -> {
            for (int i = 0; i <= 10; i++) {
                System.out.println("will take the lock from write");
            }
            try {
                lock.readLock().lock();
                System.out.println("## Read Thread : " + Thread.currentThread().getName() + " ==> " + message);
            } finally {
                lock.readLock().unlock();
            }
        };
    }

    private Runnable concatString(String str) {
        return () -> {
            for (int i = 0; i <= 10; i++) {
                try {
                    lock.writeLock().lock();
                    message = message.concat(str);
                } finally {
                    lock.writeLock(). unlock();
                }
            }
        };
    }




}

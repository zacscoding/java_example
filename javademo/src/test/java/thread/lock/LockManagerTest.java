package thread.lock;

import java.util.concurrent.TimeUnit;
import org.junit.Test;

/**
 * @author zacconding
 * @Date 2019-01-09
 * @GitHub : https://github.com/zacscoding
 */
public class LockManagerTest {

    @Test
    public void taskWithIds() throws InterruptedException {
        final LockManager lockManager = new LockManager(3);
        Thread t11 = new Thread(new Task(lockManager, 1, 0, 3));
        Thread t12 = new Thread(new Task(lockManager, 1, 0, 3));
        Thread t13 = new Thread(new Task(lockManager, 1, 0, 3));

        Thread t21 = new Thread(new Task(lockManager, 2, 0, 3));
        Thread t22 = new Thread(new Task(lockManager, 2, 0, 3));
        Thread t23 = new Thread(new Task(lockManager, 2, 0, 3));

        t11.start();
        t12.start();
        t13.start();

        t21.start();
        t22.start();
        t23.start();

        t11.join();
        t12.join();
        t13.join();

        t21.join();
        t22.join();
        t23.join();
        // Outputs
//        2 working write task ... 0
//        1 working write task ... 0
//        1 working write task ... 1
//        2 working write task ... 1
//        1 working write task ... 2
//        2 working write task ... 2
//        1 working write task ... 0
//        2 working write task ... 0
//        1 working write task ... 1
//        2 working write task ... 1
//        1 working write task ... 2
//        2 working write task ... 2
//        1 working write task ... 0
//        2 working write task ... 0
//        1 working write task ... 1
//        2 working write task ... 1
//        1 working write task ... 2
//        2 working write task ... 2
    }

    public static class Task implements Runnable {

        private LockManager lockManager;
        private Object taskIdentifier;
        private int readTask;
        private int writeTask;

        public Task(LockManager lockManager, Object taskIdentifier, int readTask, int writeTask) {
            this.lockManager = lockManager;
            this.taskIdentifier = taskIdentifier;
            this.readTask = readTask;
            this.writeTask = writeTask;
        }

        @Override
        public void run() {
            try {
                final int identifier = taskIdentifier.hashCode();

                if (readTask > 0) {
                    try {
                        lockManager.readLock(identifier).lock();
                        for (int i = 0; i < readTask; i++) {
                            System.out.println(String.format("%s working read task ... %d", identifier, i));
                            TimeUnit.SECONDS.sleep(1L);
                        }
                    } finally {
                        lockManager.readLock(identifier).unlock();
                    }
                }

                if (writeTask > 0) {
                    try {
                        lockManager.writeLock(identifier).lock();
                        for (int i = 0; i < writeTask; i++) {
                            System.out.println(String.format("%s working write task ... %d", identifier, i));
                            TimeUnit.SECONDS.sleep(1L);
                        }
                    } finally {
                        lockManager.writeLock(identifier).unlock();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

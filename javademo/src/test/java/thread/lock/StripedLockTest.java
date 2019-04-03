package thread.lock;

import com.google.common.util.concurrent.Striped;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import org.junit.Test;

/**
 * Not work.. will try again
 *
 * @GitHub : https://github.com/zacscoding
 */
public class StripedLockTest {

    @Test
    public void keyBasedLock() throws Exception {
        Striped<Lock> locks = Striped.lock(4);

        WriteTask task1 = new WriteTask("key1", "thread01", locks);
        WriteTask task2 = new WriteTask("key2", "thread02", locks);
        WriteTask task3 = new WriteTask("key3", "thread03", locks);
        WriteTask task4 = new WriteTask("key1", "thread04", locks);

        List<WriteTask> tasks = Arrays.asList(task1, task2, task3, task4);

        for (WriteTask task : tasks) {
            task.start();
        }

        for (WriteTask task : tasks) {
            task.join();
        }
    }

    public static class WriteTask extends Thread {

        final String taskKey;
        final Striped<Lock> locks;
        final String threadName;
        final int repeat;

        public WriteTask(String taskKey, String name, Striped<Lock> locks) {
            this.taskKey = taskKey;
            this.locks = locks;
            this.threadName = name;
            //this.repeat = new Random().nextInt(3) + 1;
            this.repeat = 3;
        }

        @Override
        public void run() {
            Lock lock = locks.get(taskKey);
            try {
                for (int i = 0; i < repeat; i++) {
                    System.out.printf("Work %s ... %d\n", threadName, i);
                    TimeUnit.SECONDS.sleep(1L);
                }
            } catch (Exception e) {
                e.printStackTrace(System.err);
            } finally {
                lock.unlock();
            }
        }
    }
}

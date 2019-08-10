package thread.lock;

import java.util.Arrays;
import org.testng.annotations.Test;

/**
 *
 */
public class LockObjectTest {

    public static int[] NUMBERS = new int[]{0, 0, 0};

    @Test
    public void runTests() throws Exception {
        int repeat = 3 * 10000;
        Thread[] tasks = new Thread[repeat];
        for (int i = 0; i < repeat; i++) {
            tasks[i] = new LockThread(i % 3);
            tasks[i].start();
        }

        for (int i = 0; i < repeat; i++) {
            tasks[i].join();
        }

        System.out.println(Arrays.toString(NUMBERS));
    }

    public static class LockThread extends Thread {

        private final Object LOCK = new Object();
        private int id;

        public LockThread(int id) {
            this.id = id;
        }

        @Override
        public void run() {
            synchronized (LOCK) {
                NUMBERS[id]++;
            }
        }
    }

}

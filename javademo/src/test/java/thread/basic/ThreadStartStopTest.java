package thread.basic;

import java.util.concurrent.TimeUnit;
import org.junit.Test;

/**
 * @author zacconding
 * @Date 2018-08-16
 * @GitHub : https://github.com/zacscoding
 */
public class ThreadStartStopTest {

    @Test
    public void startAndStop() throws InterruptedException {
        Thread t = new Thread(() -> {
            try {
                while (!Thread.currentThread().isInterrupted()) {
                    System.out.println("Running thread...");
                    TimeUnit.SECONDS.sleep(1L);
                }
            } catch (InterruptedException e) {
                System.out.println("Interrupted : " + e.getMessage());
            }
        });
        t.setDaemon(true);
        t.start();

        TimeUnit.SECONDS.sleep(4L);
        t.interrupt();
        System.out.println("## After calling interrupt");

        // not working
        t.start();
        TimeUnit.SECONDS.sleep(2L);
    }
}

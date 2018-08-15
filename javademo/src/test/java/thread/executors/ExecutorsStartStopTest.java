package thread.executors;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import org.junit.Test;

/**
 * @author zacconding
 * @Date 2018-08-16
 * @GitHub : https://github.com/zacscoding
 */
public class ExecutorsStartStopTest {

    @Test
    public void startAndStop() throws Exception {
        ExecutorService executorService = Executors.newSingleThreadExecutor(r -> {
            Thread t = Executors.defaultThreadFactory().newThread(r);
            t.setDaemon(true);
            return t;
        });

        final Runnable runnable = () -> {
            try {
                while (!Thread.currentThread().isInterrupted()) {
                    System.out.println("Running thread @@");
                    TimeUnit.SECONDS.sleep(1L);
                }
            } catch (InterruptedException e) {
                System.out.println("Interrupted : " + e.getMessage());
            }
        };

        executorService.submit(runnable);
        TimeUnit.SECONDS.sleep(4L);
        executorService.shutdown();

        System.out.println("## After calling interrupt");
        // not working
        executorService.submit(runnable);
        TimeUnit.SECONDS.sleep(2L);
    }
}

package thread.executors;

import static org.junit.Assert.fail;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.TimeUnit;
import org.junit.Test;
import util.SimpleLogger;

/**
 * @author zacconding
 * @Date 2018-08-16
 * @GitHub : https://github.com/zacscoding
 */
public class ExecutorsStartStopTest {

    private ExecutorService executorService;
    private Runnable runnable;

    @Test
    public void startAndStop() throws Exception {
        executorService = Executors.newSingleThreadExecutor(r -> {
            Thread t = Executors.defaultThreadFactory().newThread(r);
            t.setDaemon(true);
            return t;
        });

        runnable = () -> {
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
        System.out.println("## After sleep..");
        executorService.shutdown();
        //boolean terminate = executorService.awaitTermination(3L, TimeUnit.SECONDS);
        boolean terminate = false;
        SimpleLogger.println("# terminate : {} | isTerminated : {} | isShutdown : {}"
            , terminate, executorService.isTerminated(), executorService.isShutdown());

        try {
            // throw err
            executorService.submit(runnable);
            fail();
        } catch(RejectedExecutionException e) {

        }
    }
}

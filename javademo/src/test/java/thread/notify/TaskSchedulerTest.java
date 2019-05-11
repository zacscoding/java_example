package thread.notify;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.Test;
import thread.notify.TaskScheduler.TaskListener;
import util.SimpleLogger;

/**
 * @GitHub : https://github.com/zacscoding
 */
public class TaskSchedulerTest {

    @Test
    public void doWorkTest() throws Exception {
        TaskScheduler scheduler = new TaskScheduler(new TaskListener() {
            @Override
            public void onComplete(String taskName) {

            }
        });
        scheduler.start();
        TimeUnit.SECONDS.sleep(4L);
        SimpleLogger.info(">> Register task01, task02");
        scheduler.registerTask("task01");
        scheduler.registerTask("task02");
        TimeUnit.SECONDS.sleep(4L);
        SimpleLogger.info(">> Register task03");
        scheduler.stop();
    }
}

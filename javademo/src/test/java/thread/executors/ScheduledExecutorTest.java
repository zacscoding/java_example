package thread.executors;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import org.junit.Before;
import org.junit.Test;
import util.SimpleLogger;

/**
 * @author zacconding
 * @Date 2018-08-16
 * @GitHub : https://github.com/zacscoding
 */
public class ScheduledExecutorTest {

    private ScheduledExecutorService scheduledExecutor;
    private CountDownLatch countDownLatch;
    private Runnable runnable;
    private long initDelay;
    private long period;

    @Before
    public void setUp() {
        // scheduledExecutor = Executors.newScheduledThreadPool(1);
        scheduledExecutor = Executors.newSingleThreadScheduledExecutor(r -> {
            Thread t = Executors.defaultThreadFactory().newThread(r);
            t.setDaemon(true);
            return t;
        });
        countDownLatch = new CountDownLatch(3);
        runnable = () -> {
            SimpleDateFormat sdf = new SimpleDateFormat("yy-MM-dd :: HH:mm:ss,sss");
            System.out.println("Now : " + sdf.format(new Date()));
            countDownLatch.countDown();
        };

        initDelay = 1000L;
        period = 1000L;
    }

    @Test
    public void fixRate() throws Exception {
        SimpleLogger.println(">> Start test countDown : {} | now : {} <<"
            , countDownLatch.getCount(), new SimpleDateFormat("yy-MM-dd :: HH:mm:ss,sss").format(new Date()));

        scheduledExecutor.scheduleAtFixedRate(runnable, initDelay, period, TimeUnit.MILLISECONDS);
        countDownLatch.await(5, TimeUnit.SECONDS);
        scheduledExecutor.shutdown();
        SimpleLogger.println(">> End test countDown : {} <<", countDownLatch.getCount());
    }
}

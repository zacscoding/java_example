package timer;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import org.checkerframework.checker.units.qual.A;
import org.junit.Test;

/**
 *
 */
public class TimerDefaultTest {

    @Test
    public void defaultUseage() throws Exception {
        AtomicInteger integer = new AtomicInteger(1);
        Timer timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                System.out.println("## task is called..");
                if (integer.getAndIncrement() != 1) {
                    System.out.println("# cancel task..");
                    timer.cancel();
                }
            }
        };

        timer.schedule(timerTask, 5000L, 5000L);
        TimeUnit.MINUTES.sleep(1L);
    }

}

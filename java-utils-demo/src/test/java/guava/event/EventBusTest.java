package guava.event;

import static org.junit.Assert.assertEquals;

import com.google.common.eventbus.AllowConcurrentEvents;
import com.google.common.eventbus.AsyncEventBus;
import com.google.common.eventbus.DeadEvent;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import org.junit.Before;
import org.junit.Test;
import util.SimpleLogger;
import util.ThreadUtil;

/**
 * https://www.baeldung.com/guava-eventbus
 *
 * @author zacconding
 * @Date 2018-08-30
 * @GitHub : https://github.com/zacscoding
 */
public class EventBusTest {

    EventBus eventBus;
    AsyncEventBus asyncEventBus;

    @Before
    public void setUp() {
        eventBus = new EventBus();
        asyncEventBus = new AsyncEventBus("async", Executors.newFixedThreadPool(5));
    }

    @Test
    public void basicTest() {
        // registering listeners
        EventListener listener = new EventListener();
        eventBus.register(listener);

        // Console output :: Handle string event [main(1)] ... : This is String event
        eventBus.post("This is String event");
        assertEquals(1, EventListener.getEventsHandled());

        // unregistering listeners
        eventBus.unregister(listener);
        eventBus.post("After unregister");
        assertEquals(1, EventListener.getEventsHandled());
    }

    @Test
    public void customEvent() {
        EventListener listener = new EventListener();
        eventBus.register(listener);

        CustomEvent event = new CustomEvent("START", 2L);

        // output :: Handle custom event[main(1)] ... Action : START
        eventBus.post(event); // handle at same thread
        assertEquals(1, EventListener.getEventsHandled());
    }

    @Test
    public void customEventAsync() throws InterruptedException {
        EventListener listener = new EventListener();
        asyncEventBus.register(listener);

        CountDownLatch countDownLatch = new CountDownLatch(2);
        CustomEvent e1 = new CustomEvent("START", 2L, countDownLatch);
        CustomEvent e2 = new CustomEvent("STOP", 3L, countDownLatch);

        asyncEventBus.post(e1);
        asyncEventBus.post(e2);

        countDownLatch.await(6L, TimeUnit.SECONDS);
        // Console output

        // 1) @AllowConcurrentEvents
        /*
        Handle custom event [pool-1-thread-2(14)] ... Action : STOP | Sleep : 3
        Handle custom event [pool-1-thread-1(13)] ... Action : START | Sleep : 2
        Action-START,Sleep-2 - Complete
        Action-STOP,Sleep-3 - Complete
         */
        // 2) // @AllowConcurrentEvents
        /*
        Handle custom event [pool-1-thread-1(13)] ... Action : START | Sleep : 2
        Action-START,Sleep-2 - Complete
        Handle custom event [pool-1-thread-2(14)] ... Action : STOP | Sleep : 3
        Action-STOP,Sleep-3 - Complete
         */
    }

    public static class EventListener {

        private static int eventsHandled;

        public static int getEventsHandled() {
            return eventsHandled;
        }

        @Subscribe
        public void handleStringEvent(String event) {
            SimpleLogger.println("Handle string event [{}] ... : {}", ThreadUtil.getThreadInfo(), event);
            EventListener.eventsHandled++;
        }

        @Subscribe
        //@AllowConcurrentEvents
        private void handleCustomEvent(CustomEvent event) {
            String identifier = "Action-" + event.getAction() + ",Sleep-" + event.getSleepSecond();

            SimpleLogger.println("Handle custom event [{}] ... Action : {} | Sleep : {}"
                , ThreadUtil.getThreadInfo(), event.getAction(), event.getSleepSecond());
            if (event.getSleepSecond() > 0L) {
                try {
                    TimeUnit.SECONDS.sleep(event.getSleepSecond());
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            CountDownLatch countDownLatch = event.getCountDownLatch();
            if (countDownLatch != null) {
                countDownLatch.countDown();
            }
            SimpleLogger.println("{} - Complete", identifier);
            EventListener.eventsHandled++;
        }

        @Subscribe
        public void handleDeadEvent(DeadEvent deadEvent) {
            SimpleLogger.println("Handle dead event [{}] ...[{}] ", ThreadUtil.getThreadInfo(), deadEvent);
            EventListener.eventsHandled++;
        }
    }

    public static class CustomEvent {

        private String action;
        private Long sleepSecond;
        private CountDownLatch countDownLatch;

        public CustomEvent(String action) {
            this.action = action;
        }

        public CustomEvent(String action, long sleepSecond) {
            this.action = action;
            this.sleepSecond = sleepSecond;
        }

        public CustomEvent(String action, Long sleepSecond, CountDownLatch countDownLatch) {
            this.action = action;
            this.sleepSecond = sleepSecond;
            this.countDownLatch = countDownLatch;
        }

        public String getAction() {
            return action;
        }

        public void setAction(String action) {
            this.action = action;
        }

        public Long getSleepSecond() {
            return sleepSecond;
        }

        public void setSleepSecond(Long sleepSecond) {
            this.sleepSecond = sleepSecond;
        }

        public CountDownLatch getCountDownLatch() {
            return countDownLatch;
        }

        public void setCountDownLatch(CountDownLatch countDownLatch) {
            this.countDownLatch = countDownLatch;
        }
    }
}
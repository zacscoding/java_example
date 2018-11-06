package events;

import events.DefaultEvent.DefaultEventType;
import events.listeners.DefaultEventListener;
import events.publishers.DefaultEventPublisher;
import java.util.concurrent.TimeUnit;
import org.junit.Test;

/**
 * @author zacconding
 * @Date 2018-11-06
 * @GitHub : https://github.com/zacscoding
 */
public class DefaultEventTest {

    @Test
    public void publishEvent() throws InterruptedException {

        DefaultEventPublisher publisher = new DefaultEventPublisher();
        DefaultEventListener listener = new DefaultEventListener();
        publisher.register(listener);

        publisher.publish(new DefaultOneEvent(DefaultEventType.EVENT_TYPE1));
        publisher.publish(new DefaultTwoEvent(DefaultEventType.EVENT_TYPE2));

        TimeUnit.SECONDS.sleep(2L);
    }
}

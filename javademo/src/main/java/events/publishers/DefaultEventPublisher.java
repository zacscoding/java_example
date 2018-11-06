package events.publishers;

import com.google.common.eventbus.AsyncEventBus;
import com.google.common.eventbus.EventBus;
import events.DefaultEvent;
import java.util.concurrent.Executors;
import util.SimpleLogger;

/**
 * @author zacconding
 * @Date 2018-11-06
 * @GitHub : https://github.com/zacscoding
 */
public class DefaultEventPublisher {

    private final EventBus eventBus;

    public DefaultEventPublisher() {
        // this.eventBus = new EventBus();
        this.eventBus = new AsyncEventBus("default-event-bus", Executors.newSingleThreadExecutor());
    }

    /**
     * Publish event
     */
    public void publish(DefaultEvent event) {
        SimpleLogger.println("publish.. {}", Thread.currentThread().getName());
        eventBus.post(event);
    }

    /**
     * Register a listener
     */
    public void register(Object object) {
        this.eventBus.register(object);
    }
}

package demo.event;

import com.google.common.eventbus.AsyncEventBus;
import com.google.common.eventbus.EventBus;
import demo.event.BlockEvent;
import demo.util.CollectorThreadFactory;
import java.util.Objects;
import java.util.concurrent.Executors;

/**
 * @GitHub : https://github.com/zacscoding
 */
public class BlockPublisher {

    private EventBus asyncEventBus;

    public BlockPublisher() {
        this.asyncEventBus = new AsyncEventBus("block-event-bus",
            Executors.newCachedThreadPool(new CollectorThreadFactory("block-publisher", true)));
    }

    public void publish(final BlockEvent blockEvent) {
        if (blockEvent == null) {
            return;
        }

        asyncEventBus.post(blockEvent);
    }

    public void register(Object listener) {
        Objects.requireNonNull(listener, "listener must be not null");
        asyncEventBus.register(listener);
    }
}

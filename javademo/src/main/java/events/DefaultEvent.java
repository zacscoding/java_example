package events;

/**
 * @author zacconding
 * @Date 2018-11-06
 * @GitHub : https://github.com/zacscoding
 */
public abstract class DefaultEvent {

    protected final DefaultEventType eventType;

    public DefaultEvent(DefaultEventType eventType) {
        this.eventType = eventType;
    }

    public DefaultEventType getType() {
        return this.eventType;
    }

    public enum DefaultEventType {
        EVENT_TYPE1, EVENT_TYPE2;
    }
}
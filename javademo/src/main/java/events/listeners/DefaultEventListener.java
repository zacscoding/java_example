package events.listeners;

import com.google.common.eventbus.Subscribe;
import events.DefaultOneEvent;
import events.DefaultTwoEvent;
import util.SimpleLogger;

/**
 * @author zacconding
 * @Date 2018-11-06
 * @GitHub : https://github.com/zacscoding
 */
public class DefaultEventListener {

    @Subscribe
    public void onDefaultTwoEvent(DefaultTwoEvent event) {
        SimpleLogger.println("onDefaultTwoEvent(DefaultTwoEvent event) :: " + event.getType());
    }

    @Subscribe
    public void onDefaultOneEvent(DefaultOneEvent event) {
        SimpleLogger.println("onDefaultOneEvent(DefaultOneEvent event) :: " + event.getType());
    }
}
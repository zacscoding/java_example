package test.action;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import java.util.Objects;
import java.util.concurrent.BlockingQueue;
import util.SimpleLogger;

/**
 * @author zacconding
 * @Date 2018-08-14
 * @GitHub : https://github.com/zacscoding
 */
public class DefaultActionListener implements ActionListener {

    private BlockingQueue<Action> actionQueue;

    @Inject
    public DefaultActionListener(@Named("ACTION_QUEUE") BlockingQueue<Action> actionQueue) {
        Objects.requireNonNull(actionQueue, "ActionQueue must be not null");
        this.actionQueue = actionQueue;
    }

    @Override
    public void requestAction(Action action) {
        SimpleLogger.println("[ActionListener] Request action... | QueueSize : {} | Action : {}", actionQueue.size(), action);
        actionQueue.offer(action);
    }
}

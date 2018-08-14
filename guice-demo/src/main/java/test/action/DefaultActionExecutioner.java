package test.action;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import java.util.Objects;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import util.SimpleLogger;

/**
 * @author zacconding
 * @Date 2018-08-14
 * @GitHub : https://github.com/zacscoding
 */
public class DefaultActionExecutioner implements ActionExecutioner {

    private BlockingQueue<Action> actionQueue;
    private Thread actionExecutor;
    private boolean isExecuting;

    @Inject
    public DefaultActionExecutioner(@Named("ACTION_QUEUE") BlockingQueue<Action> actionQueue) {
        Objects.requireNonNull(actionQueue, "ActionQueue must be not null");
        this.actionQueue = actionQueue;

        actionExecutor = new Thread(() -> {
            try {
                while (!Thread.currentThread().isInterrupted()) {
                    Action action = actionQueue.take();
                    isExecuting = true;
                    SimpleLogger.println("[ActionExecutioner] take action : " + action.toString());
                    System.out.println("## Execute Action : " + action.toString());
                    TimeUnit.SECONDS.sleep(2L);
                    isExecuting = false;
                    SimpleLogger.println("[ActionExecutioner] complete action : " + action.toString());
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        actionExecutor.setDaemon(true);
        actionExecutor.start();
    }

    @Override
    public boolean isExecuting() {
        return isExecuting;
    }
}
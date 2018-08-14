package test.action;

import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Provides;
import com.google.inject.Scopes;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author zacconding
 * @Date 2018-08-14
 * @GitHub : https://github.com/zacscoding
 */
public class ActionModule extends AbstractModule {

    @Override
    protected void configure() {

        bind(ActionListener.class).toProvider(ActionListenerProvider.class).in(Scopes.SINGLETON);
        bind(ActionExecutioner.class).toProvider(ActionExecutionerProvider.class).in(Scopes.SINGLETON);
    }

    @Provides
    @Named("ACTION_QUEUE")
    @Singleton
    public BlockingQueue<Action> provideActionQueue() {
        return new LinkedBlockingQueue<>();
    }
}

class ActionListenerProvider implements Provider<ActionListener> {

    private BlockingQueue<Action> actionQueue;

    @Inject
    public ActionListenerProvider(@Named("ACTION_QUEUE") BlockingQueue<Action> actionQueue) {
        this.actionQueue = actionQueue;
        System.out.println("ActionListenerProvider() :: " + actionQueue);
    }

    @Override
    public ActionListener get() {
        return new DefaultActionListener(actionQueue);
    }
}

class ActionExecutionerProvider implements Provider<ActionExecutioner> {

    private BlockingQueue<Action> actionQueue;

    @Inject
    public ActionExecutionerProvider(@Named("ACTION_QUEUE") BlockingQueue<Action> actionQueue) {
        this.actionQueue = actionQueue;
        System.out.println("ActionExecutionerProvider() :: " + actionQueue);
    }

    @Override
    public ActionExecutioner get() {
        return new DefaultActionExecutioner(actionQueue);
    }
}
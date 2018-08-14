package test;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.TypeLiteral;
import com.google.inject.name.Named;
import com.google.inject.name.Names;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import util.SimpleLogger;

/**
 * @author zacconding
 * @Date 2018-08-14
 * @GitHub : https://github.com/zacscoding
 */
public class QueueInjectTest {

    public static void main(String[] args) {

        Injector injector = Guice.createInjector(new QueueInjectModule());
        BlockingQueue<Integer> actionQueue = injector.getInstance(Key.get(new TypeLiteral<BlockingQueue<Integer>>(){}, Names.named("ACTION_QUEUE")));
        SimpleLogger.println("## Check action queue.\ninfo : {}\nsize : {}\npeek : {}"
            ,(actionQueue.getClass().getName() + "::"+ toString(actionQueue)), actionQueue.size(), actionQueue.peek());

        actionQueue.offer(1);

        BlockingQueue<Integer> actionQueue2 = injector.getInstance(Key.get(new TypeLiteral<BlockingQueue<Integer>>(){}, Names.named("ACTION_QUEUE")));
        SimpleLogger.println("## Check action queue.\ninfo : {}\nsize : {}\npeek : {}"
            ,(actionQueue2.getClass().getName() + "::"+ toString(actionQueue2)), actionQueue2.size(), actionQueue2.peek());

        BlockingQueue<String> messageQueue = injector.getInstance(Key.get(new TypeLiteral<BlockingQueue<String>>(){}, Names.named("MESSAGE_QUEUE")));
        SimpleLogger.println("## Check message queue.\ninfo : {}\nsize : {}\npeek : {}"
            ,(messageQueue.getClass().getName() + "::"+ toString(messageQueue)), messageQueue.size(), messageQueue.peek());

        messageQueue.offer("FIRST");
        BlockingQueue<String> messageQueue2 = injector.getInstance(Key.get(new TypeLiteral<BlockingQueue<String>>(){}, Names.named("MESSAGE_QUEUE")));
        SimpleLogger.println("## Check message queue.\ninfo : {}\nsize : {}\npeek : {}"
            ,(messageQueue2.getClass().getName() + "::"+ toString(messageQueue2)), messageQueue2.size(), messageQueue2.peek());

    }

    private static String toString(Object object) {
        if (object == null) {
            return "null";
        }
        return object.getClass().getName() + "@" + Integer.toHexString(object.hashCode());
    }
}

class QueueInjectModule extends AbstractModule {

    @Override
    protected void configure() {
    }

    @Provides
    @Named("ACTION_QUEUE")
    @Singleton
    public BlockingQueue<Integer> provideActionQueue() {
        return new LinkedBlockingQueue<>();
    }

    @Provides
    @Named("MESSAGE_QUEUE")
    @Singleton
    public BlockingQueue<String> provideMessageQueue() {
        return new LinkedBlockingQueue<>();
    }
}

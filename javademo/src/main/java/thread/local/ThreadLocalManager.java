package thread.local;

/**
 * @author zacconding
 * @Date 2018-03-01
 * @GitHub : https://github.com/zacscoding
 */
public class ThreadLocalManager {

    private static ThreadLocal<ThreadLocalContext> contexts = new ThreadLocal<>();

    public static ThreadLocalContext getOrCreate() {
        ThreadLocalContext ctx = null;

        if ((ctx = contexts.get()) == null) {
            ctx = new ThreadLocalContext();
            contexts.set(ctx);
        }

        return ctx;
    }

    public static ThreadLocalContext clear() {
        ThreadLocalContext ctx = null;

        // set null or remove
        if ((ctx = contexts.get()) != null) {
            contexts.remove();
            // contexts.set(null);
        }

        return ctx;
    }
}

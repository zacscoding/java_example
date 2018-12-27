package pool.connection;

import java.util.concurrent.atomic.AtomicInteger;
import util.SimpleLogger;

/**
 * @author zacconding
 * @Date 2018-12-27
 * @GitHub : https://github.com/zacscoding
 */
public class ConnectionClient {

    private AtomicInteger rand = new AtomicInteger(0);
    private int id;

    public ConnectionClient(int id) {
        this.id = id;
    }

    /**
     * Check connection alive
     */
    public boolean isAlive() {
        boolean result = rand.getAndIncrement() % 2 == 0;
        SimpleLogger.println("{} try to isAlive.. result : {}", toString(), result);
        return result;
    }

    public void close() {
        SimpleLogger.println("{} close() is called", toString());
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return getClass().getName() + "@" + Integer.toHexString(hashCode()) + "-" + id;
    }
}

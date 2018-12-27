package pool.connection;

import java.util.concurrent.atomic.AtomicInteger;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import util.SimpleLogger;

/**
 * @author zacconding
 * @Date 2018-12-27
 * @GitHub : https://github.com/zacscoding
 */
public class ConnectionClientFactory implements PooledObjectFactory<ConnectionClient> {

    private AtomicInteger idGenerator = new AtomicInteger(0);

    @Override
    public PooledObject<ConnectionClient> makeObject() throws Exception {
        int id = idGenerator.getAndIncrement();

        ConnectionClient client = new ConnectionClient(id);

        SimpleLogger.println("makeObject() : {}", client.toString());

        return new DefaultPooledObject<>(client);
    }

    @Override
    public void destroyObject(PooledObject<ConnectionClient> pool) throws Exception {
        ConnectionClient client = pool.getObject();
        if (client == null) {
            SimpleLogger.println("destroyObject() but null");
            return;
        }

        SimpleLogger.println("destroyObject() : {}", client);

        if (client.isAlive()) {
            client.close();
        }
    }

    @Override
    public boolean validateObject(PooledObject<ConnectionClient> pool) {
        ConnectionClient client = pool.getObject();

        if (client != null) {
            SimpleLogger.println("validateObject() : {}", client);
            boolean result = client.isAlive();
            SimpleLogger.println("validateObject() : {} > {}", client, result);
            return result;
        }

        SimpleLogger.println("validateObject() but null");

        return false;
    }

    @Override
    public void activateObject(PooledObject<ConnectionClient> pool) throws Exception {
        SimpleLogger.println("activateObject() is called : {}", pool.getObject());
    }

    @Override
    public void passivateObject(PooledObject<ConnectionClient> pool) throws Exception {
        SimpleLogger.println("passivateObject() is called : {}", pool.getObject());
    }
}

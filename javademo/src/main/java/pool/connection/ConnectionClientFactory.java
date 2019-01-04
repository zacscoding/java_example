package pool.connection;

import java.net.URI;
import java.util.List;
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

    private AtomicInteger index;
    private List<URI> uris;

    public ConnectionClientFactory(List<URI> uris) {
        this.uris = uris;
        this.index = new AtomicInteger(0);
    }

    @Override
    public PooledObject<ConnectionClient> makeObject() throws Exception {
        int idx = index.getAndIncrement() % uris.size();
        ConnectionClient client = new ConnectionClient(uris.get(idx));

        SimpleLogger.println("[CONN-CLIENT-FACTORY] makeObject(). idx : {} | uri : {}", idx, client.getUri());

        return new DefaultPooledObject<>(client);
    }

    @Override
    public void destroyObject(PooledObject<ConnectionClient> pool) throws Exception {
        ConnectionClient client = pool.getObject();
        if (client == null) {
            SimpleLogger.println("[CONN-CLIENT-FACTORY] destroyObject() but null");
            return;
        }

        SimpleLogger.println("[CONN-CLIENT-FACTORY] destroyObject() : {}", client);

        if (client.isAlive()) {
            client.close();
        }
    }

    @Override
    public boolean validateObject(PooledObject<ConnectionClient> pool) {
        ConnectionClient client = pool.getObject();

        if (client != null) {
            SimpleLogger.println(
                "[CONN-CLIENT-FACTORY] Validation {} >> {}", client.getUri(), client.isAlive()
            );

            return client.isAlive();
        }

        return false;
    }

    @Override
    public void activateObject(PooledObject<ConnectionClient> pool) throws Exception {
        ConnectionClient client = pool.getObject();

        if (client != null && client.isAlive()) {
            SimpleLogger.println("[CONN-CLIENT-FACTORY] activeObject() is called : {}", client);
            return;
        }

        SimpleLogger.println("[CONN-CLIENT-FACTORY] activeObject() is called : {} but throw ex", client);
        throw new Exception();
    }

    @Override
    public void passivateObject(PooledObject<ConnectionClient> pool) throws Exception {
        SimpleLogger.println("[CONN-CLIENT-FACTORY] passivateObject() is called : {}", pool.getObject());

        ConnectionClient client = pool.getObject();

        if (client != null && client.isAlive()) {
            return;
        }

        throw new Exception();
    }
}

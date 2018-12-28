package pool.connection;

import java.net.URI;
import java.util.List;
import java.util.Random;
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

    private Random indexGenerator;
    private List<URI> uris;

    public ConnectionClientFactory(List<URI> uris) {
        this.uris = uris;
    }

    @Override
    public PooledObject<ConnectionClient> makeObject() throws Exception {
        ConnectionClient client = new ConnectionClient(uris.get(indexGenerator.nextInt(uris.size())));
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
            return client.isAlive();
        }

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

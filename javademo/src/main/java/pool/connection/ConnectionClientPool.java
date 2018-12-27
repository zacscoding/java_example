package pool.connection;

import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

/**
 * @author zacconding
 * @Date 2018-12-27
 * @GitHub : https://github.com/zacscoding
 */
public class ConnectionClientPool extends GenericObjectPool<ConnectionClient> {

    public ConnectionClientPool(PooledObjectFactory<ConnectionClient> factory, GenericObjectPoolConfig config) {
        super(factory, config);
    }
}

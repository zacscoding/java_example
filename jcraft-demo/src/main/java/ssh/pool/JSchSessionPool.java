package ssh.pool;

import com.jcraft.jsch.Session;
import org.apache.commons.pool2.KeyedPooledObjectFactory;
import org.apache.commons.pool2.impl.GenericKeyedObjectPool;
import org.apache.commons.pool2.impl.GenericKeyedObjectPoolConfig;
import ssh.ServerDetails;

/**
 * @author zacconding
 * @Date 2019-01-01
 * @GitHub : https://github.com/zacscoding
 */
public class JSchSessionPool extends GenericKeyedObjectPool<ServerDetails, Session> {

    public JSchSessionPool(KeyedPooledObjectFactory<ServerDetails, Session> factory) {
        super(factory, new GenericKeyedObjectPoolConfig<>());
    }

    public JSchSessionPool(KeyedPooledObjectFactory<ServerDetails, Session> factory,
        GenericKeyedObjectPoolConfig<Session> config) {

        super(factory, config);
    }
}

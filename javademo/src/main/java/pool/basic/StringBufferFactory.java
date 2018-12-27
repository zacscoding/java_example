package pool.basic;

import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import util.SimpleLogger;

/**
 * @author zacconding
 * @Date 2018-12-27
 * @GitHub : https://github.com/zacscoding
 */
public class StringBufferFactory extends BasePooledObjectFactory<StringBuffer> {

    @Override
    public StringBuffer create() throws Exception {
        StringBuffer buff = new StringBuffer();

        SimpleLogger.println("create() : {}", toString(buff));

        return buff;
    }

    /**
     * Use the default PooledObject implementation.
     */
    @Override
    public PooledObject<StringBuffer> wrap(StringBuffer buffer) {
        return new DefaultPooledObject<>(buffer);
    }

    /**
     * When an object is returned to the pool, clear the buffer.
     */
    @Override
    public void passivateObject(PooledObject<StringBuffer> pooledObject) {
        StringBuffer buff = pooledObject.getObject();
        SimpleLogger.println("passivateObject() : {}", toString(buff));
        buff.setLength(0);
    }

    private String toString(StringBuffer buff) {
        return buff.getClass().getName() + "@" + Integer.toHexString(buff.hashCode());
    }
}

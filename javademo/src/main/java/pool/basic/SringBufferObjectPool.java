package pool.basic;

import java.io.IOException;
import java.io.Reader;
import org.apache.commons.pool2.ObjectPool;
import util.SimpleLogger;

/**
 * https://commons.apache.org/proper/commons-pool/examples.html
 */
public class SringBufferObjectPool {

    private ObjectPool<StringBuffer> pool;

    public SringBufferObjectPool(ObjectPool<StringBuffer> pool) {
        this.pool = pool;
    }

    public String readToString(Reader in) throws IOException {
        StringBuffer buff = null;

        try {
            buff = pool.borrowObject();
            String address = buff.getClass().getName() + "@" + Integer.toHexString(buff.hashCode());
            SimpleLogger.println("borrowObject() : {}", address);

            for (int c = in.read(); c != -1; c = in.read()) {
                buff.append((char) c);
            }

            return buff.toString();
        } catch (IOException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Unable to borrow buffer from pool" + e.toString());
        } finally {
            try {
                in.close();
            } catch (Exception e) {
            }

            if (buff != null) {
                try {
                    pool.returnObject(buff);
                } catch (Exception e) {
                }
            }
        }
    }

}

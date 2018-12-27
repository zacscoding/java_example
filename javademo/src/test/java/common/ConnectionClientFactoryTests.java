package common;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.junit.Test;
import pool.connection.ConnectionClient;
import pool.connection.ConnectionClientFactory;
import pool.connection.ConnectionClientPool;
import util.SimpleLogger;

/**
 * @author zacconding
 * @Date 2018-12-27
 * @GitHub : https://github.com/zacscoding
 */
public class ConnectionClientFactoryTests {

    @Test
    public void test() throws Exception {
        GenericObjectPoolConfig config = new GenericObjectPoolConfig();
        config.setMaxIdle(5);
        config.setMinIdle(1);
        config.setMaxTotal(4);
        config.setTestOnBorrow(true);
        config.setTestOnCreate(true);
        config.setTestOnReturn(true);
        config.setJmxEnabled(false);
        config.setBlockWhenExhausted(true);

        ConnectionClientPool pool = new ConnectionClientPool(new ConnectionClientFactory(), config);

        Thread[] tasks = new Thread[10];
        for (int i = 0; i < 10; i++) {
            tasks[i] = new Thread(() -> {
                try {
                    ConnectionClient client = pool.borrowObject();
                    push(client.toString());
                    pool.returnObject(client);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

            tasks[i].start();
        }

        for (int i = 0; i < 10; i++) {
            tasks[i].join();
        }

        TimeUnit.SECONDS.sleep(5L);

        addrs.iterator().forEachRemaining(
            addr -> System.out.println("## created client :: " + addr)
        );
        SimpleLogger.println("## Created {} clients", addrs.size());
    }

    Set<String> addrs = new HashSet<>();

    private synchronized void push(String addr) {
        addrs.add(addr);
    }
}

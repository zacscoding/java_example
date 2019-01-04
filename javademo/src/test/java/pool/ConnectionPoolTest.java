package pool;

import static org.junit.Assert.fail;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.junit.Before;
import org.junit.Test;
import pool.connection.ConnectionClient;
import pool.connection.ConnectionClientFactory;
import pool.connection.ConnectionClientPool;
import util.SimpleLogger;

/**
 * @author zacconding
 * @Date 2019-01-04
 * @GitHub : https://github.com/zacscoding
 */
@Slf4j
public class ConnectionPoolTest {

    List<URI> servers;
    List<ServerSocket> mockServers;

    @Before
    public void setUp() throws IOException {
        servers = Arrays.asList(
            URI.create("http://127.0.0.1:11001"),
            URI.create("http://127.0.0.1:11002"),
            URI.create("http://127.0.0.1:11003")
        );

        mockServers = new ArrayList<>();

        for (URI uri : servers) {
            ServerSocket serverSocket = new ServerSocket();
            mockServers.add(serverSocket);
            serverSocket.bind(new InetSocketAddress(uri.getHost(), uri.getPort()));
        }
    }

    @Test
    public void pooling() throws Exception {
        ConnectionClientFactory factory = new ConnectionClientFactory(servers);

        GenericObjectPoolConfig config = new GenericObjectPoolConfig();
        ConnectionClientPool pool = new ConnectionClientPool(factory, config);

        ConnectionClient[] clients = new ConnectionClient[servers.size()];
        final CountDownLatch countDownLatch = new CountDownLatch(servers.size());

        for (int i = 0; i < servers.size(); i++) {
            Runnable runnable = () -> {
                try {
                    ConnectionClient client = pool.borrowObject();
                    SimpleLogger.println("## Receive client : {}", client.getUri());
                    TimeUnit.SECONDS.sleep(1L);
                    pool.returnObject(client);
                    countDownLatch.countDown();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            };
            Thread t = new Thread(runnable);
            t.setDaemon(true);
            t.start();
            /*SimpleLogger.println("## Receive client : {}", clients[i].getUri());
            TimeUnit.SECONDS.sleep(1L);*/
        }

        countDownLatch.await();

        // stop 1
        mockServers.get(1).close();

        /*for (int i = 0; i < servers.size(); i++) {
            pool.returnObject(clients[i]);
        }*/

        final CountDownLatch countDownLatch1 = new CountDownLatch(servers.size());

        for (int i = 0; i < servers.size(); i++) {
            Runnable runnable = () -> {
                try {
                    ConnectionClient client = pool.borrowObject();
                    if (client.getUri().equals(servers.get(1))) {
                        fail();
                    }
                    SimpleLogger.println("## Receive client : {}", client.getUri());
                    countDownLatch1.countDown();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            };

            Thread t = new Thread(runnable);
            t.setDaemon(true);
            t.start();
        }

        countDownLatch1.await();

        for (ServerSocket mockServer : mockServers) {
            mockServer.close();
        }

        // Output
//[CONN-CLIENT-FACTORY] makeObject(). idx : 0 | uri : http://127.0.0.1:11001[CONN-CLIENT-FACTORY] makeObject(). idx : 1 | uri : http://127.0.0.1:11002
//[CONN-CLIENT-FACTORY] makeObject(). idx : 2 | uri : http://127.0.0.1:11003
//
//[CONN-CLIENT-FACTORY] activeObject() is called : http://127.0.0.1:11001
//[CONN-CLIENT-FACTORY] activeObject() is called : http://127.0.0.1:11002
//[CONN-CLIENT-FACTORY] activeObject() is called : http://127.0.0.1:11003
//## Receive client : http://127.0.0.1:11002
//## Receive client : http://127.0.0.1:11001
//## Receive client : http://127.0.0.1:11003
//[CONN-CLIENT-FACTORY] passivateObject() is called : http://127.0.0.1:11002
//[CONN-CLIENT-FACTORY] passivateObject() is called : http://127.0.0.1:11003
//[CONN-CLIENT-FACTORY] passivateObject() is called : http://127.0.0.1:11001
//[CONN-CLIENT-FACTORY] activeObject() is called : http://127.0.0.1:11001
//[CONN-CLIENT-FACTORY] activeObject() is called : http://127.0.0.1:11003
//## Receive client : http://127.0.0.1:11001
//## Receive client : http://127.0.0.1:11003
//[CONN-CLIENT-FACTORY] activeObject() is called : http://127.0.0.1:11002 but throw ex
//[CONN-CLIENT-FACTORY] destroyObject() : http://127.0.0.1:11002
//[CONN-CLIENT-FACTORY] makeObject(). idx : 0 | uri : http://127.0.0.1:11001
//[CONN-CLIENT-FACTORY] activeObject() is called : http://127.0.0.1:11001
//## Receive client : http://127.0.0.1:11001
    }
}

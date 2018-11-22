package protocol.websocket;

import dto.Pair;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * WebSocket client manager
 *
 * - getting active websocket client
 * - try to maintain connections
 *
 * @author zacconding
 * @Date 2018-11-20
 * @GitHub : https://github.com/zacscoding
 */
public class WebSocketManager {

    private Object lock;
    private ExecutorService retryExecutorService;
    private List<MockWebSocketClient> activeClients;
    private List<Pair<MockWebSocketClient, Integer>> inActiveClients;
    private ReentrantReadWriteLock readWriteLock;

    // for mock
    public WebSocketManager(List<MockWebSocketClient> webSocketClients) {
        this.retryExecutorService = Executors.newSingleThreadExecutor();
        this.activeClients = new ArrayList<>(webSocketClients.size());
        this.inActiveClients = new ArrayList<>(webSocketClients.size());
        this.readWriteLock = new ReentrantReadWriteLock();

        boolean existNotConnected = false;

        for (MockWebSocketClient webSocketClient : webSocketClients) {
            try {
                webSocketClient.addListener(() -> tryReconnect());
                boolean isConnected = webSocketClient.connect();
                if (!isConnected) {
                    throw new Exception();
                }
                activeClients.add(webSocketClient);
            } catch (Exception e) {
                existNotConnected = true;
                inActiveClients.add(new Pair<>(webSocketClient, 1));
            }
        }

        if (existNotConnected) {
            tryReconnect();
        }
    }

    private void checkActiveClients() {
        try {
            readWriteLock.writeLock().lock();
            for (int i = 0; i < activeClients.size(); i++) {
                MockWebSocketClient mockWebSocketClient = activeClients.get(i);

                if (mockWebSocketClient.isConnected()) {
                    continue;
                }

                activeClients.remove(i);
                inActiveClients.add(new Pair<>(mockWebSocketClient, 1));
                i--;
            }
        } finally {
            readWriteLock.writeLock().unlock();
        }
    }

    private void tryReconnect() {
        if (retryExecutorService.isShutdown()) {

        }
    }
}
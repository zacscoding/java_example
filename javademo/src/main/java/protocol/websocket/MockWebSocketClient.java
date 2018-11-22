package protocol.websocket;

import java.util.LinkedList;
import java.util.List;

/**
 * @author zacconding
 * @Date 2018-11-20
 * @GitHub : https://github.com/zacscoding
 */
public class MockWebSocketClient {

    private String url;
    private boolean isConnected;
    private List<MockWebSocketListener> webSocketListeners;

    public MockWebSocketClient(String url) {
        this.url = url;
        this.webSocketListeners = new LinkedList<>();
    }

    public String getUrl() {
        return url;
    }

    public void setConnected(boolean connected) {
        isConnected = connected;
    }

    public boolean isConnected() {
        return isConnected;
    }

    public void addListener(MockWebSocketListener webSocketListener) {
        webSocketListeners.add(webSocketListener);
    }

    public boolean connect() {
        return isConnected;
    }

    public void close() {
        this.isConnected = false;
        webSocketListeners.forEach(MockWebSocketListener::onClose);
    }
}
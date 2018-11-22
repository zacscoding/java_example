package protocol.websocket;

import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.junit.Test;

/**
 * @author zacconding
 * @Date 2018-11-20
 * @GitHub : https://github.com/zacscoding
 */
public class WebSocketConnectAsyncTest {

    final ExecutorService executor = Executors.newCachedThreadPool();

    @Test
    public void connect() {
        MockWebSocketClient webSocketClient = new MockWebSocketClient("aa");
    }

    public <T> CompletableFuture<T> run(Callable<T> callable) {
        CompletableFuture<T> result = new CompletableFuture();

        CompletableFuture.runAsync(() -> {
            try {
                result.complete(callable.call());
            } catch (Throwable var3) {
                result.completeExceptionally(var3);
            }
        }, executor);

        return result;
    }
}

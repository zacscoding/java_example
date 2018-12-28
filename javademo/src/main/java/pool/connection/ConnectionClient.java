package pool.connection;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.URI;
import org.springframework.util.SocketUtils;
import util.SimpleLogger;

/**
 * @author zacconding
 * @Date 2018-12-27
 * @GitHub : https://github.com/zacscoding
 */
public class ConnectionClient {

    private URI uri;

    public ConnectionClient(URI uri) {
        this.uri = uri;
    }

    /**
     * Check connection alive
     */
    public boolean isAlive() {
        try {
            Socket socket = new Socket();
            socket.connect(new InetSocketAddress(uri.getHost(), uri.getPort()), 1000);
            socket.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public void close() {
        SimpleLogger.println("{} close() is called", toString());
    }

    public URI getUri() {
        return uri;
    }

    @Override
    public String toString() {
        return uri.toString();
    }
}

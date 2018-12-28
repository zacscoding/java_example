import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.URI;
import java.net.URISyntaxException;
import org.junit.Test;
import util.SimpleLogger;

/**
 * @author zacconding
 * @Date 2018-10-08
 * @GitHub : https://github.com/zacscoding
 */
public class Temp {

    @Test
    public void temp() throws URISyntaxException {
        System.out.println(ping("ws://192.168.5.78:8450/"));
    }

    public boolean ping(String url) throws URISyntaxException {
        long start = System.currentTimeMillis();

        URI uri = new URI(url);
        SimpleLogger.println("Try to host: {} | port : {}", uri.getHost(), uri.getPort());
        SocketAddress socketAddress = new InetSocketAddress(uri.getHost(), uri.getPort());
        Socket socket = new Socket();

        try {
            socket.connect(socketAddress, 3000);
            return true;
        } catch (Exception e) {
            return false;
        } finally {
            if (socket != null && socket.isConnected()) {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            long elapsed = System.currentTimeMillis() - start;
            System.out.println(elapsed + " [MS]");
        }
    }
}

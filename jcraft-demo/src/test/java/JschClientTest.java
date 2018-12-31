import com.jcraft.jsch.JSchException;
import java.nio.charset.Charset;
import org.junit.Before;
import org.junit.Test;
import ssh2222.JschClient;

/**
 * @author zacconding
 * @Date 2018-11-07
 * @GitHub : https://github.com/zacscoding
 */
public class JschClientTest {

    private String host;
    private Integer port;
    private String user;
    private byte[] password;
    private byte[] privatekey;

    @Before
    public void setUp() {
        host = "192.168.5.78";
        port = Integer.valueOf(22);
        user = "app";
        password = "apppw".getBytes(Charset.defaultCharset());
    }

    @Test
    public void temp() {
        try (JschClient jschClient = JschClient.startClient(host, port, user, password, privatekey)) {
            // 1) execute and get result (sync)

            // 2) execute and handler result (async)
            // jschClient.execute();
        } catch (JSchException e) {

        }
    }

}

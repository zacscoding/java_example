package ssh2222;

import java.io.IOException;

/**
 * @author zacconding
 * @Date 2018-11-02
 * @GitHub : https://github.com/zacscoding
 */
public interface RemoteSession {

    Process exec(String commandName, int connectTimeoutSeconds) throws IOException;

    Process exec(String commandName, int connectTimeoutSeconds, long executeTimeout) throws IOException;

    void disconnect();
}
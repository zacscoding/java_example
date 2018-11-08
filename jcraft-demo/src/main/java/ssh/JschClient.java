package ssh;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

/**
 * @author zacconding
 * @Date 2018-11-07
 * @GitHub : https://github.com/zacscoding
 */
public class JschClient implements AutoCloseable {

    private static final int DEFAULT_CONNECTION_TIMEOUT_SECOND = 5;
    private static final int DEFAULT_PORT = 22;

    private String host;
    private Integer port;
    private String user;
    private byte[] password;
    private byte[] privateKey;

    private JschSession jschSession;

    public static JschClient startClient(String host, Integer port, String user, byte[] password, byte[] privateKey) throws JSchException {
        if (port == null) {
            port = DEFAULT_PORT;
        }

        return new JschClient(host, port, user, password, privateKey);
    }

    public JschClient(String host, Integer port, String user, byte[] password, byte[] privateKey) throws JSchException {
        this.host = host;
        this.port = port;
        this.user = user;
        this.password = password;
        this.privateKey = privateKey;

        connect();
    }

    /**
     * @param type :  "sftp" || "session" || "shell" || "exec" || "x11" || "auth-agent@openssh.com" || "direct-tcpip" || "forwarded-tcpip" || "subsystem"
     */
    public Channel getChannel(String type) throws JSchException {
        return jschSession.getChannel(type);
    }

  /*
  public ExecuteResult executeAndGetResult(String command, long timeout, ProcessExecuteHandler executorHandler) throws IOException {
        Process process = jschSession.exec(command, DEFAULT_CONNECTION_TIMEOUT_SECOND);
        JschExecutorHandler handler = new JschExecutorHandler(process);

        ExecuteWatchdog watchDog = null;
        boolean useWatchDog = timeout > 0L;

        if (useWatchDog) {
            watchDog = new ExecuteWatchdog(timeout);
            handler.setWatchdog(watchDog);
        }

        ExecuteLogOutputStream stdout = new ExecuteLogOutputStream(executorHandler, true, collectLogOutput);
        ExecuteLogOutputStream stderr = new ExecuteLogOutputStream(executorHandler, false, collectLogOutput);

        if (useWatchDog && watchDog.killedProcess()) {

        }

        // JschExecutorHandler handler = new JschExecutorHandler();
        return null;
    }*/

    private void connect() throws JSchException {
        JSch jSch = new JSch();
        if (privateKey != null && privateKey.length > 0) {
            jSch.addIdentity(host, privateKey, null, null);
        }

        Session session = jSch.getSession(user, host, port);
        session.setConfig("StrictHostKeyChecking", "no");

        if (password != null && password.length > 0) {
            session.setPassword(password);
        }

        session.connect();
        jschSession = new JschSession(session);
    }

    @Override
    public void close() {
        jschSession.disconnect();
    }
}

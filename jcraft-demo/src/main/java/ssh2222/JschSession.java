package ssh2222;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.LinkedList;
import java.util.Queue;

/**
 * @author zacconding
 * @Date 2018-11-02
 * @GitHub : https://github.com/zacscoding
 */
public class JschSession implements RemoteSession {

    private Object rwMutex;
    private final Session sock;
    private Queue<Channel> channels;

    public JschSession(Session session) {
        this.sock = session;
        channels = new LinkedList<>();
        rwMutex = new Object();
    }

    public Channel getChannel(String type) throws JSchException {
        Channel c = sock.openChannel(type);
        if (c != null) {
            synchronized (rwMutex) {
                channels.offer(c);
            }
        }
        return c;
    }

    @Override
    public Process exec(String commandName, int connectTimeoutSeconds) throws IOException {
        return exec(commandName, connectTimeoutSeconds, -1L);
    }

    @Override
    public Process exec(String commandName, int connectTimeoutSeconds, long executeTimeout) throws IOException {
        return new JschProcess(commandName, connectTimeoutSeconds);
    }

    @Override
    public void disconnect() {
        // close channels
        synchronized (rwMutex) {
            while (!channels.isEmpty()) {
                Channel c = channels.poll();
                if (c.isConnected()) {
                    c.disconnect();
                }
            }
        }

        // close session
        if (sock != null && sock.isConnected()) {
            sock.disconnect();
        }
    }

    private class JschProcess extends Process {

        private ChannelExec channel;
        private InputStream inputStream;
        private OutputStream outputStream;
        private InputStream errStream;

        private JschProcess(String commandName, int connectionTimeoutSeconds) throws IOException {
            try {
                channel = (ChannelExec) sock.openChannel("exec");
                channel.setCommand(commandName);

                setupStreams();

                channel.connect(connectionTimeoutSeconds > 0 ? connectionTimeoutSeconds : 0);
            } catch (JSchException e) {
                close(outputStream);
                throw new RuntimeException(e);
            }
        }

        private void setupStreams() throws IOException {
            inputStream = channel.getInputStream();
            OutputStream out = channel.getOutputStream();
            outputStream = out;
            errStream = channel.getErrStream();
        }

        private boolean isRunning() {
            return channel.getExitStatus() < 0 && channel.isConnected();
        }

        @Override
        public OutputStream getOutputStream() {
            return outputStream;
        }

        @Override
        public InputStream getInputStream() {
            return inputStream;
        }

        @Override
        public InputStream getErrorStream() {
            return errStream;
        }

        @Override
        public int waitFor() throws InterruptedException {
            while (isRunning()) {
                Thread.sleep(20L);
            }

            return exitValue();
        }

        @Override
        public int exitValue() {
            while (isRunning()) {
                throw new IllegalStateException();
            }

            return channel.getExitStatus();
        }

        @Override
        public void destroy() {
            if (channel.isConnected()) {
                channel.disconnect();
            }

            close(outputStream);
        }

        public Throwable close(OutputStream os) {
            try {
                if (os != null) {
                    os.close();
                }
            } catch (Throwable e) {
                return e;
            }

            return null;
        }
    }
}

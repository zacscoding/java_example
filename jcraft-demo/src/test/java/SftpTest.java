import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.ChannelSftp.LsEntry;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpATTRS;
import com.jcraft.jsch.SftpException;
import demo.util.SimpleLogger;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Vector;
import org.junit.Test;

/**
 * https://steemit.com/kr-dev/@capslock/java-sftp-using-jsch-sftp
 */
public class SftpTest {

    String host = "192.168.5.77";
    String user = "testuser";
    int port = 22;
    String password = "test";

    File uploadFile = new File("E:\\test\\test.txt");
    String remoteDestDir = "/home/testuser/testdir";

    JSch jsch;
    Session session;
    Channel channel;
    ChannelSftp channelSftp;

    @Test
    public void test() {
        try {
            step("Connect", this::connect);
            step("Upload", this::upload);
            step("Download", this::download);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (session != null && session.isConnected()) {
                if (channelSftp != null) {
                    channelSftp.disconnect();
                }
                if (channel != null) {
                    channel.disconnect();
                }
                session.disconnect();
            }
        }
    }

    private void connect() throws Exception {
        jsch = new JSch();
        session = jsch.getSession(user, host, port);
        session.setConfig("StrictHostKeyChecking", "no");
        session.setPassword(password);
        session.connect();

        channel = session.openChannel("sftp");
        channel.connect();
        channelSftp = (ChannelSftp) channel;
        System.out.println("## Success to connect");
    }

    private void upload() throws Exception {
        rmWithRecursive(remoteDestDir);
        channelSftp.mkdir(remoteDestDir);
        channelSftp.cd(remoteDestDir);

        boolean isFile = false;
        String chmodVal = "777";
        int chmod = Integer.parseInt(chmodVal, 8);

        if (isFile) {
            try (FileInputStream fis = new FileInputStream(uploadFile)) {
                channelSftp.put(fis, uploadFile.getName());
                String fullPath = remoteDestDir + "/" + uploadFile.getName();
                channelSftp.chmod(chmod, fullPath);
                SimpleLogger.println("## Success to upload. full path : {} || chmod : {}", fullPath, chmodVal);
            }
        } else {
            String text = "test1 \ntest2\n";
            InputStream is = new ByteArrayInputStream(text.getBytes(StandardCharsets.UTF_8));
            String fullPath = remoteDestDir + "/" + uploadFile.getName();
            channelSftp.put(is, fullPath);
            channelSftp.chmod(chmod, fullPath);
            SimpleLogger.println("## Success to upload text. full path : {} || chmod : {}", fullPath, chmodVal);
        }
    }

    private void download() throws Exception {
        channelSftp.cd(remoteDestDir);
        InputStream is = channelSftp.get(uploadFile.getName());
        String content = new String(readAll(is));
        System.out.println("Read content \n" + content);
    }

    private void step(String title, DoTask task) {
        System.out.println("// ================================================================================================================ ");
        System.out.println("----> Start to " + title);

        try {
            task.doSomething();
        } catch (Exception e) {
            System.out.println("## Error : " + e.getMessage());
        }

        System.out
            .println("=================================================================================================================== //\n\n");
    }

    private void rmWithRecursive(String path) throws Exception {
        System.out.println("Remove : " + path);
        try {
            if (channelSftp.stat(path).isDir()) {
                Vector<LsEntry> files = channelSftp.ls(path);
                if (files != null && files.size() > 0) {
                    Iterator<LsEntry> itr = files.iterator();
                    while (itr.hasNext()) {
                        LsEntry entry = itr.next();
                        if ((!entry.getFilename().equals(".")) && (!entry.getFilename().equals(".."))) {
                            rmWithRecursive(path + "/" + entry.getFilename());
                        }
                    }
                }
                channelSftp.rmdir(path);
            } else {
                channelSftp.rm(path);
            }
        } catch (NullPointerException e) {
            return;
        } catch (SftpException e) {
            return;
        } catch (Exception e) {
            throw e;
        }
    }

    private byte[] readAll(InputStream is) throws IOException {
        if (is == null) {
            return null;
        }

        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        int read;
        byte[] data = new byte[1024];

        while ((read = is.read(data, 0, data.length)) != -1) {
            buffer.write(data, 0, read);
        }

        buffer.flush();

        return buffer.toByteArray();
    }

    @FunctionalInterface
    interface DoTask {

        void doSomething() throws Exception;
    }
}

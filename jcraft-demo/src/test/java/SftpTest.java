import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.ChannelSftp.LsEntry;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpATTRS;
import java.io.File;
import java.io.FileInputStream;
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
    }

    private void upload() throws Exception {
        channel = session.openChannel("sftp");
        channel.connect();

        channelSftp = (ChannelSftp) channel;
        rmWithRecursive(remoteDestDir);
        channelSftp.mkdir(remoteDestDir);
        channelSftp.cd(remoteDestDir);

        try (FileInputStream fis = new FileInputStream(uploadFile)) {
            channelSftp.put(fis, uploadFile.getName());
            System.out.println("## Success to upload");
        }
    }

    private void step(String title, DoTask task) throws Exception {
        System.out.println("======================================================== ");
        System.out.println(title);
        task.doSomething();
        System.out.println("======================================================== //\n\n");
    }

    private void rmWithRecursive(String path) throws Exception {
        System.out.println("Remove : " + path);
        SftpATTRS stat = channelSftp.stat(path);
        if (stat == null) {
            return;
        }

        if (stat.isDir()) {
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
    }

    @FunctionalInterface
    interface DoTask {

        void doSomething() throws Exception;
    }
}

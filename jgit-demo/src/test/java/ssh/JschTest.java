package ssh;

import org.eclipse.jgit.errors.TransportException;
import org.eclipse.jgit.transport.CredentialsProvider;
import org.eclipse.jgit.transport.JschConfigSessionFactory;
import org.eclipse.jgit.transport.RemoteSession;
import org.eclipse.jgit.transport.SshSessionFactory;
import org.eclipse.jgit.transport.TransportSftp;
import org.eclipse.jgit.transport.URIish;
import org.eclipse.jgit.util.FS;
import org.junit.Test;

/**
 * @author zacconding
 * @Date 2018-07-05
 * @GitHub : https://github.com/zacscoding
 */
public class JschTest {

    @Test
    public void test() {
        SshSessionFactory sessionFactory = JschConfigSessionFactory.getInstance();
        //getSession(URIish uri, CredentialsProvider credentialsProvider, FS fs, int tms) throws TransportException {
        URIish urIish = new URIish();

        // sessionFactory.getSession();


    }

}

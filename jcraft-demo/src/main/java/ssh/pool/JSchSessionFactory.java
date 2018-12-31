package ssh.pool;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import java.nio.charset.Charset;
import org.apache.commons.pool2.BaseKeyedPooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import ssh.ServerDetails;

/**
 * @author zacconding
 * @Date 2019-01-01
 * @GitHub : https://github.com/zacscoding
 */
public class JSchSessionFactory extends BaseKeyedPooledObjectFactory<ServerDetails, Session> {

    @Override
    public Session create(ServerDetails serverDetails) throws Exception {
        // log.info("Create Session : hash : {}", serverDetails.hashCode());

        JSch jSch = new JSch();

        if (serverDetails.getPrivateKey() != null) {
            jSch.addIdentity(
                serverDetails.getHost(), serverDetails.getPrivateKey().getBytes(Charset.forName("UTF-8"))
                , null, null
            );
        }

        Session session = jSch.getSession(
            serverDetails.getUsername(), serverDetails.getHost(), serverDetails.getPort()
        );
        session.setConfig("StrictHostKeyChecking", "no");

        session.setUserInfo(serverDetails.getUserInfo());
        int timeout = serverDetails.getConnectTimeOut() >= 0 ? serverDetails.getConnectTimeOut() : 0;
        session.setTimeout(timeout);

        if (serverDetails.getUserInfo().getPassword() != null) {
            session.setPassword(serverDetails.getUserInfo().getPassword());
        }

        return session;
    }

    @Override
    public PooledObject<Session> wrap(Session session) {
        return new DefaultPooledObject<>(session);
    }

    @Override
    public void activateObject(ServerDetails key, PooledObject<Session> pool) throws Exception {
        Session session = pool.getObject();

        if (session != null) {
            //log.info("Active session : {} - {}", simpleSessionInfo(session), session);
            session.connect();
        }
    }

    @Override
    public boolean validateObject(ServerDetails key, PooledObject<Session> pool) {
        Session session = pool.getObject();

        if (session != null) {
            // log.info("Connect session : {} - {}", simpleSessionInfo(session), session);
            return session.isConnected();
        }

        return false;
    }

    @Override
    public void destroyObject(ServerDetails key, PooledObject<Session> pool) throws Exception {
        Session session = pool.getObject();

        if (session != null) {
            // log.info("DisConnect session : {} - {}", simpleSessionInfo(session), session);
            session.disconnect();
        }
    }

    private String simpleSessionInfo(Session session) {
        if (session == null) {
            return "null";
        }

        return new StringBuilder(session.getUserName())
            .append('@')
            .append(session.getHost())
            .append(':')
            .append(session.getPort())
            .toString();
    }
}

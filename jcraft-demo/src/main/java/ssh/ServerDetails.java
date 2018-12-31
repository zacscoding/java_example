package ssh;

import com.jcraft.jsch.UserInfo;

/**
 * @author zacconding
 * @Date 2019-01-01
 * @GitHub : https://github.com/zacscoding
 */
public class ServerDetails {

    private String host;
    private String username;
    private int port;
    private int connectTimeOut;
    private String privateKey;

    private UserInfoImpl userInfo;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getConnectTimeOut() {
        return connectTimeOut;
    }

    public void setConnectTimeOut(int connectTimeOut) {
        this.connectTimeOut = connectTimeOut;
    }

    public String getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }

    public UserInfoImpl getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfoImpl userInfo) {
        this.userInfo = userInfo;
    }

    public static class UserInfoImpl implements UserInfo {

        private String user;
        private String password;

        public String getUser() {
            return user;
        }

        public void setUser(String user) {
            this.user = user;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        @Override
        public String getPassphrase() {
            return password;
        }

        @Override
        public String getPassword() {
            return password;
        }

        @Override
        public boolean promptPassword(String message) {
            return true;
        }

        @Override
        public boolean promptPassphrase(String message) {
            return true;
        }

        @Override
        public boolean promptYesNo(String message) {
            return false;
        }

        @Override
        public void showMessage(String message) {
            //
        }
    }


}

package demo;

import java.util.List;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

/**
 * https://www.tutorialspoint.com/zookeeper/zookeeper_api.htm
 */
public class ZKGetChildren {

    private static ZooKeeper zk;
    private static ZooKeeperConnection conn;

    public static Stat znodeExists(String path) throws KeeperException, InterruptedException {
        return zk.exists(path, true);
    }

    /**
     * >> before running
     * [zk: localhost:2181(CONNECTED) 4] create /MyFirstZnode/myfirstsubnode Hi
     * Created /MyFirstZnode/myfirstsubnode
     * [zk: localhost:2181(CONNECTED) 5] create /MyFirstZnode/mysecondsubmode Hi
     * Created /MyFirstZnode/mysecondsubmode
     */
    public static void main(String[] args) {
        String path = "/MyFirstZnode";

        try {
            conn = new ZooKeeperConnection();
            zk = conn.connect("192.168.5.77");

            Stat stat = znodeExists(path);

            if (stat != null) {
                List<String> children = zk.getChildren(path, false);
                for (int i = 0; i < children.size(); i++) {
                    System.out.println(children.get(i));
                }
            } else {
                System.out.println("Not exist :: " + path);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

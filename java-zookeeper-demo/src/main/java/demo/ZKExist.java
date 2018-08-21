package demo;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

/**
 * https://www.tutorialspoint.com/zookeeper/zookeeper_api.htm
 */
public class ZKExist {

    private static ZooKeeper zk;
    private static ZooKeeperConnection conn;

    public static Stat znodeExists(String path) throws KeeperException, InterruptedException {
        return zk.exists(path, true);
    }

    public static void main(String[] args) {
        String path = "/MyFirstZnode";

        try {
            conn = new ZooKeeperConnection();
            zk = conn.connect("192.168.5.77");
            Stat stat = znodeExists(path); // Stat checks the path of the znode

            if (stat != null) {
                System.out.println("Find node :: " + path);
                System.out.println(stat.toString());
                System.out.println(">> Version : " + stat.getVersion());

            } else {
                System.out.println("Not exist node :: " + path);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

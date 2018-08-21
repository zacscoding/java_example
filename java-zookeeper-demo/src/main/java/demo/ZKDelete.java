package demo;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooKeeper;

/**
 * @author zacconding
 * @Date 2018-08-21
 * @GitHub : https://github.com/zacscoding
 */
public class ZKDelete {

    private static ZooKeeper zk;
    private static ZooKeeperConnection conn;

    public static void delete(String path) throws KeeperException, InterruptedException {
        // path :: Znode path
        // version :: Current version of the znode
        zk.delete(path, zk.exists(path, true).getVersion());
    }

    public static void main(String[] args) {
        String path = "/MyFirstZone";

        try {
            conn = new ZooKeeperConnection();
            zk = conn.connect("192.168.5.77");
            delete(path); //delete the node with the specified path
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

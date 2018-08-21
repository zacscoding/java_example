package demo;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;

/**
 * https://www.tutorialspoint.com/zookeeper/zookeeper_api.htm
 */
public class ZKCreate {

    private static ZooKeeper zk;

    private static ZooKeeperConnection conn;

    public static void create(String path, byte[] data) throws KeeperException, InterruptedException {
        // path :: Znode path
        // data :: data to store in a specified znode path
        // acl  :: access control list of the node to be created
        // createMode :: the type of node
        zk.create(path, data, Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
    }

    public static void main(String[] args) {
        String path = "/MyFirstZnode";
        byte[] data = "My first zookeeper app".getBytes();

        try {
            conn = new ZooKeeperConnection();
            zk = conn.connect("192.168.5.77");
            create(path, data); // Create the data to the specified path
            conn.close();
        } catch (Exception e) {
            System.out.println(e.getMessage()); //Catch error message
        }

        // check
        /*
        [zk: localhost:2181(CONNECTED) 0] get /MyFirstZnode
        My first zookeeper app
        cZxid = 0x12
        ctime = Tue Aug 21 21:41:31 KST 2018
        mZxid = 0x12
        mtime = Tue Aug 21 21:41:31 KST 2018
        pZxid = 0x12
        cversion = 0
        dataVersion = 0
        aclVersion = 0
        ephemeralOwner = 0x0
        dataLength = 22
        numChildren = 0
         */

    }
}

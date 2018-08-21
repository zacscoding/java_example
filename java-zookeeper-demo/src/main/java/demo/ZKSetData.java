package demo;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooKeeper;

/**
 * https://www.tutorialspoint.com/zookeeper/zookeeper_api.htm
 */
public class ZKSetData {

    private static ZooKeeper zk;
    private static ZooKeeperConnection conn;

    public static void update(String path, byte[] data) throws KeeperException, InterruptedException {
        zk.setData(path, data, zk.exists(path, true).getVersion());
    }

    /**
     * after running
     *
     *[zk: localhost:2181(CONNECTED) 3] get /MyFirstZnode
     * Success
     * cZxid = 0x12
     * ctime = Tue Aug 21 21:41:31 KST 2018
     * mZxid = 0x1f
     * mtime = Tue Aug 21 22:09:46 KST 2018
     * pZxid = 0x12
     * cversion = 0
     * dataVersion = 2
     * aclVersion = 0
     * ephemeralOwner = 0x0
     * dataLength = 7
     * numChildren = 0
     */
    public static void main(String[] args) {
        String path = "/MyFirstZnode";
        byte[] data = "Success".getBytes();//Assign data which is to be updated.

        try {
            conn = new ZooKeeperConnection();
            zk = conn.connect("192.168.5.77");
            update(path, data);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

package demo;

import java.util.concurrent.CountDownLatch;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.EventType;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

/**
 * https://www.tutorialspoint.com/zookeeper/zookeeper_api.htm
 */
public class ZKGetData {

    private static ZooKeeper zk;
    private static ZooKeeperConnection conn;

    public static Stat znodeExists(String path) throws KeeperException, InterruptedException {
        return zk.exists(path, true);
    }

    /**
     * after running
     * [zk: localhost:2181(CONNECTED) 2] set /MyFirstZnode HelloZookeeper
     *
     * console output :
     * My first zookeeper app
     * HelloZookeeper
     */
    public static void main(String[] args) {
        String path = "/MyFirstZnode";
        final CountDownLatch connectedSignal = new CountDownLatch(1);

        try {
            conn = new ZooKeeperConnection();
            zk = conn.connect("192.168.5.77");
            Stat stat = znodeExists(path);

            if (stat != null) {
                byte[] b = zk.getData(path, new Watcher() {
                    @Override
                    public void process(WatchedEvent watchedEvent) {
                        if (watchedEvent.getType() == EventType.None) {
                            switch (watchedEvent.getState()) {
                                case Expired:
                                    connectedSignal.countDown();
                                    break;
                            }
                        } else {
                            String path = "/MyFirstZnode";

                            try {
                                byte[] bn = zk.getData(path, false, null);
                                String data = new String(bn, "UTF-8");
                                System.out.println(data);
                                connectedSignal.countDown();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }, null);

                String data = new String(b, "UTF-8");
                System.out.println(data);
                connectedSignal.await();
            } else {
                System.out.println("Not exist node :: " + path);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

}

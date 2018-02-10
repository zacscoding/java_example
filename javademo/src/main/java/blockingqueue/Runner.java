package blockingqueue;

import com.sun.org.apache.xpath.internal.SourceTree;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import util.ThreadUtil;

/**
 * @author zacconding
 * @Date 2018-02-08
 * @GitHub : https://github.com/zacscoding
 */
public class Runner {

    public static void main(String[] args) {
        BlockingQueue queue = new LinkedBlockingQueue();
        Thread produce = new Thread(new Producer(queue));
        Thread consume = new Thread(new Consumer(queue));

        produce.start();
        consume.start();

        ThreadUtil.sleep(20 * 10000);
    }


}

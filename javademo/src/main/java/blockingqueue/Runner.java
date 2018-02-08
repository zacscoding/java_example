package blockingqueue;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

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
        Thread consume2 = new Thread(new Consumer(queue));

        produce.start();
        consume.start();
        //consume2.start();
    }
}

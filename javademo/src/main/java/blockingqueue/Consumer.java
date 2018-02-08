package blockingqueue;

import java.util.concurrent.BlockingQueue;
import util.SimpleLogger;

/**
 * @author zacconding
 * @Date 2018-02-08
 * @GitHub : https://github.com/zacscoding
 */
public class Consumer implements Runnable {

    private BlockingQueue<String> que;

    public Consumer(BlockingQueue<String> que) {
        this.que = que;
    }

    @Override
    public void run() {
        while (true) {
            try {
                SimpleLogger.info("Before consume(que.take())...");
                String msg = que.take();
                SimpleLogger.info("After consum(que.take()) :: message : {}, thread : {}", msg, Thread.currentThread().getName());
                //Thread.sleep(3000);
            } catch (Exception e) {
                SimpleLogger.error("error", e);
            }
        }
    }
}

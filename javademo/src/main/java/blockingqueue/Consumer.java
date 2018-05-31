package blockingqueue;

import java.util.concurrent.BlockingQueue;
import util.SimpleLoggers;

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
        try {
            while (true) {
                SimpleLoggers.info("Before consume(que.take())...");
                String msg = que.take();
                SimpleLoggers.info("After consum(que.take()) :: message : {}, thread : {}", msg, Thread.currentThread().getName());
                Thread.sleep(10L);
            }
        } catch (Exception e) {
            SimpleLoggers.error("error", e);
        }
    }
}

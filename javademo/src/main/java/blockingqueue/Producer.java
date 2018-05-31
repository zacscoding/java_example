package blockingqueue;

import java.util.concurrent.BlockingQueue;
import util.SimpleLoggers;

/**
 * @author zacconding
 * @Date 2018-02-08
 * @GitHub : https://github.com/zacscoding
 */
public class Producer implements Runnable {

    private BlockingQueue<String> que;

    public Producer(BlockingQueue<String> que) {
        this.que = que;
    }

    @Override
    public void run() {
        int i = 1;
        try {
            while (true) {
                String message = "message" + (i++);
                que.offer(message);
                long wait = (long) (Math.random() * 8000L) + 3000L;
                SimpleLoggers.info("Try to produce.... message : {}, next wait : {}", message, wait);
                Thread.sleep(wait);
            }
        } catch (Exception e) {
            SimpleLoggers.error("error", e);
        }
    }
}

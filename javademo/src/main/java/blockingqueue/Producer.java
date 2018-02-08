package blockingqueue;

import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import util.SimpleLogger;

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
        while (true) {
            try {
                // String randomUUID = UUID.randomUUID().toString();
                String message = "message" + (i++);
                long wait = (long) (Math.random() * 8000L) + 3000L;
                SimpleLogger.info("Try to produce.... message : {}, next wait : {}", message, wait);
                que.add(message);
                Thread.sleep(wait);
            } catch (Exception e) {
                SimpleLogger.error("error", e);
            }
        }
    }
}

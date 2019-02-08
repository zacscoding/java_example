package blockingqueue;

import java.util.Random;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.TimeUnit;
import org.apache.commons.collections.comparators.ReverseComparator;
import org.junit.Before;
import org.junit.Test;

/**
 * @GitHub : https://github.com/zacscoding
 */
public class PriorityBlockingQueueDemo {

    Thread producer;
    Thread consumer;

    @Before
    public void setUp() {
        PriorityBlockingQueue<Integer> que = new PriorityBlockingQueue<>(11, new ReverseComparator());
        producer = new Thread(new PriorityQueueProducer(que));
        consumer = new Thread(new PriorityQueueConsumer(que));
        producer.setDaemon(true);
        consumer.setDaemon(true);
    }

    @Test
    public void startWork() throws Exception {
        consumer.start();
        producer.start();

        TimeUnit.MINUTES.sleep(1L);

        consumer.interrupt();
        producer.interrupt();

        /* output ::
        [Producer] produce : 11
        [Consumer] consume: 11 >> Will wait 3 secs
        [Producer] produce : 94
        [Producer] produce : 72
        [Producer] produce : 74
        [Consumer] consume: 72 >> Will wait 4 secs
        [Producer] produce : 58
        [Producer] produce : 83
        [Producer] produce : 29
        [Consumer] consume: 29 >> Will wait 4 secs
        [Producer] produce : 18
        [Producer] produce : 23
        [Producer] produce : 38
        [Producer] produce : 96
        [Consumer] consume: 18 >> Will wait 3 secs
        [Producer] produce : 42
        [Producer] produce : 47
        [Producer] produce : 77
        [Consumer] consume: 23 >> Will wait 4 secs
        [Producer] produce : 49
        [Producer] produce : 4
        [Producer] produce : 55
        [Producer] produce : 37
        [Consumer] consume: 4 >> Will wait 4 secs
        [Producer] produce : 57
         */
    }

    public static class PriorityQueueProducer implements Runnable {

        private PriorityBlockingQueue<Integer> queue;
        private Random random;

        public PriorityQueueProducer(PriorityBlockingQueue<Integer> queue) {
            this.queue = queue;
            this.random = new Random();
        }

        @Override
        public void run() {
            try {
                while (!Thread.currentThread().isInterrupted()) {
                    Integer number = random.nextInt(100);
                    System.out.printf("[Producer] produce : %d\n", number);
                    queue.offer(number);
                    TimeUnit.SECONDS.sleep(1L);
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    private static class PriorityQueueConsumer implements Runnable {

        private PriorityBlockingQueue<Integer> queue;
        private Random random;

        public PriorityQueueConsumer(PriorityBlockingQueue<Integer> queue) {
            this.queue = queue;
            this.random = new Random();
        }

        @Override
        public void run() {
            try {
                while (!Thread.currentThread().isInterrupted()) {
                    Integer take = queue.take();
                    int sleepSeconds = random.nextInt(5);
                    System.out.printf("[Consumer] consume: %d >> Will wait %d secs\n", take, sleepSeconds);
                    TimeUnit.SECONDS.sleep(sleepSeconds);
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}

package collector;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingQueue;
import util.SimpleLoggers;

/**
 * Test Collector
 *
 * @author zacconding
 * @Date 2018-02-10
 * @GitHub : https://github.com/zacscoding
 */
public class TestCollector {

    private static final boolean USE_BLOCKING_QUEUE = false;
    private static TestCollector INSTANCE;
    private static Object lock = new Object();
    private static final int MAX_SIZE = 20;
    private static long PERIOD = 3000L;
    private static long MAX_TIME = 10000L;

    private BlockingQueue<String> blockingQueue;
    private ConcurrentLinkedQueue<String> concurrentQueue;

    private long lastExcuteTime;
    private ISendApi sendApi;
    private Thread sendTask;

    public static TestCollector getInstance() {
        if (INSTANCE == null) {
            synchronized (lock) {
                if (INSTANCE == null) {
                    INSTANCE = new TestCollector();
                }
            }
        }

        return INSTANCE;
    }

    public void setMessage(String message) {
        if (message == null || message.length() == 0) {
            return;
        }
        if (USE_BLOCKING_QUEUE) {
            blockingQueue.offer(message);
        } else {
            concurrentQueue.offer(message);
        }
    }

    private TestCollector() {
        init();
    }

    private void init() {
        sendApi = new ISendApi() {
            @Override
            public void sendData(String data) {
                sendDatas(Arrays.asList(data));
            }

            @Override
            public void sendDatas(List<String> data) {
                SimpleLoggers.println("@@ Send data... size : {}", (data == null ? 0 : data.size()));
                StringBuilder sb = new StringBuilder();
                if (data != null) {
                    data.forEach(d -> {
                        sb.append(d).append(" ");
                    });
                    SimpleLoggers.println(sb.toString());
                }
            }
        };

        if (USE_BLOCKING_QUEUE) {
            blockingQueue = new LinkedBlockingQueue<>();
            sendTask = new Thread(new SendOneTask());
        } else {
            concurrentQueue = new ConcurrentLinkedQueue<>();
            sendTask = new Thread(new SendMultipleTask());
        }

        sendTask.setDaemon(true);
        sendTask.start();
    }

    private class SendOneTask implements Runnable {

        public SendOneTask() {
            if (blockingQueue == null) {
                blockingQueue = new LinkedBlockingQueue<>();
            }
        }

        @Override
        public void run() {
            try {
                while (true) {
                    String data = blockingQueue.take();
                    sendApi.sendData(data);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private class SendMultipleTask implements Runnable {

        @Override
        public void run() {
            try {
                while (true) {
                    SimpleLoggers.info("## Check que...");
                    long now = System.currentTimeMillis();
                    if ((!concurrentQueue.isEmpty()) && ((concurrentQueue.size() >= MAX_SIZE) || (lastExcuteTime + MAX_TIME <= now))) {
                        // send
                        int size = Math.min(concurrentQueue.size(), MAX_SIZE);
                        List<String> data = new ArrayList<>(size);
                        for (int i = 0; i < size; i++) {
                            data.add(concurrentQueue.poll());
                        }

                        sendApi.sendDatas(data);
                        lastExcuteTime = now;
                    }

                    Thread.sleep(PERIOD);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}

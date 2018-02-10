package collector;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import util.SimpleLogger;
import util.ThreadUtil;

/**
 * Test Collector
 *
 * @author zacconding
 * @Date 2018-02-10
 * @GitHub : https://github.com/zacscoding
 */
public class TestCollector {

    private static TestCollector INSTANCE;
    private static Object lock = new Object();
    private static final int MAX_SIZE = 20;
    private static long PERIOD = 3000L;
    private static long MAX_TIME = 10000L;

    private BlockingQueue<String> que;
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
        que.offer(message);
    }

    private TestCollector() {
        init();
    }

    private void init() {
        que = new LinkedBlockingQueue<>();
        sendApi = new ISendApi() {
            @Override
            public void sendDatas(List<String> data) {
                SimpleLogger.println("@@ Send data... size : {}", (data == null ? 0 : data.size()));
                StringBuilder sb = new StringBuilder();
                if (data != null) {
                    data.forEach(d -> {
                        sb.append(d).append(" ");
                    });
                    SimpleLogger.println(sb.toString());
                }
            }
        };

        if (sendTask == null) {
            sendTask = new Thread(new Runnable() {
                @Override
                public void run() {
                    SimpleLogger.info("## TestCollector run() is started..");
                    while (true) {
                        SimpleLogger.info("## Check que...");
                        long now = System.currentTimeMillis();
                        if ((!que.isEmpty()) && ((que.size() >= MAX_SIZE) || (lastExcuteTime + MAX_TIME <= now))) {
                            // send
                            int size = Math.min(que.size(), MAX_SIZE);
                            List<String> data = new ArrayList<String>(size);
                            for (int i = 0; i < size; i++) {
                                data.add(que.poll());
                            }
                            sendApi.sendDatas(data);
                            lastExcuteTime = now;
                        }
                        ThreadUtil.sleep(PERIOD);
                    }
                }
            });

            sendTask.setDaemon(true);
            sendTask.start();
        }
    }
}

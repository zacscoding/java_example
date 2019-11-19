package sync;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 *
 * @GitHub : https://github.com/zacscoding
 */
public class Sync {

    SyncTask task;

    public void start() {
        if (task != null) {
            System.out.println("Already started");
            return;
        }

        task = new SyncTask();
        task.start();
    }

    public void stop() {
        if (task == null) {
            System.out.println("Already stopped");
            return;
        }

        task.interrupt();
        task = null;
    }

    public void newPeer() {
        if (task != null) {
            task.newPeer();
        }
    }

    public static class SyncTask extends Thread {

        private BlockingQueue<SyncEvent> eventQueue;
        private boolean isSyncing;

        public SyncTask() {
            super.setDaemon(true);
            eventQueue = new LinkedBlockingQueue<>();
        }

        @Override
        public void run() {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    SyncEvent event = eventQueue.poll(3L, TimeUnit.SECONDS);
                    if (event == null) {
                        event = SyncEvent.TIME_LIMIT;
                    }

                    switch (event) {
                        case NEW_PEER:
                            System.out.println(getPrefix() + " new peer event occur..");
                            synchronize();
                            break;
                        case TIME_LIMIT:
                            System.out.println(getPrefix() + " time limit occur..");
                            synchronize();
                            break;
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace(System.err);
                    break;
                }
            }
        }

        public void newPeer() {
            eventQueue.offer(SyncEvent.NEW_PEER);
        }

        private void synchronize() {
            synchronized (this) {
                if (isSyncing) {
                    return;
                }
                isSyncing = true;
                Thread t = new Thread(() -> {
                    try {
                        long sleep = new Random().nextInt(10);
                        System.out.println(getPrefix() + " start to synchronize with sleep " + sleep);
                        TimeUnit.SECONDS.sleep(sleep);
                        System.out.println(getPrefix() + " complete to synchronize");

                        synchronized (SyncTask.this) {
                            isSyncing = false;
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                });
                t.setDaemon(true);
                t.start();
            }
        }

        private String getPrefix() {
            return "[" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")) + "]";
        }
    }
}

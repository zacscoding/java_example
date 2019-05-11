package thread.notify;

import java.util.Stack;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import util.SimpleLogger;

/**
 * @GitHub : https://github.com/zacscoding
 */
public class TaskScheduler implements Runnable {

    public static Object wakeupSyncObject = new Object();

    private volatile boolean shouldRun = true;
    private ReentrantReadWriteLock lock;
    private Stack<String> tasks;
    private TaskListener listener;
    private Thread schedulerThread;

    public TaskScheduler(TaskListener listener) {
        this.listener = listener;
        this.tasks = new Stack<>();
        this.lock = new ReentrantReadWriteLock();
        this.schedulerThread = new Thread(this, "TaskScheduler");
        this.schedulerThread.setDaemon(true);
        this.schedulerThread.start();
    }

    public void registerTask(String taskName) {
        try {
            lock.writeLock().lock();
            tasks.push(taskName);
            awake();
        } finally {
            lock.writeLock().unlock();
        }
    }

    public void start() {
        schedulerThread = new Thread(this);
        schedulerThread.start();
    }

    public void stop() {
        shouldRun = false;
        schedulerThread.interrupt();
    }

    public void awake() {
        synchronized (wakeupSyncObject) {
            wakeupSyncObject.notify();
        }
    }

    @Override
    public void run() {
        while (shouldRun) {
            try {
                synchronized (wakeupSyncObject) {
                    if (tasks.isEmpty()) {
                        wakeupSyncObject.wait(3000L);
                    }
                }
                doWork();
            } catch (InterruptedException e) {
                shouldRun = false;
            }
        }
    }

    private void doWork() {
        SimpleLogger.info("Start task executor");
        try {
            lock.readLock().lock();
            if (tasks.isEmpty()) {
                return;
            }
            String taskName = tasks.pop();
            SimpleLogger.info("Do task : {}", taskName);
            listener.onComplete(taskName);
        } finally {
            lock.readLock().unlock();
        }
    }

    public interface TaskListener {

        void onComplete(String taskName);
    }
}

package nio.file.watch;

import java.io.File;
import java.io.IOException;
import java.nio.file.ClosedWatchServiceException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author zacconding
 * @Date 2018-08-02
 * @GitHub : https://github.com/zacscoding
 */
public class FileWatcher {

    private static final Logger logger = LoggerFactory.getLogger(FileWatcher.class);
    public static FileWatcher INSTANCE = new FileWatcher();

    private static final Object LOCK = new Object();
    private ThreadGroup threadGroup;
    private Map<String, Long> threadIds;

    public FileWatcher() {
        initialize();
    }

    public boolean regist(File file, FileModifiedListener listener) {
        if (file == null || file.isDirectory()) {
            logger.warn("Failed to regist file. file is null or directory. file : {}", file);
            return false;
        }

        try {
            Path path = file.toPath();
            Path parentPath = path.getParent();
            String parentDir = parentPath.toAbsolutePath().toString();
            String fileName = path.getFileName().toString();

            WatcherThread watcherThread = getThread(parentDir, false);
            if (watcherThread == null) {
                synchronized (LOCK) {
                    if ((watcherThread = getThread(parentDir, false)) == null) {
                        WatchService watcher = FileSystems.getDefault().newWatchService();
                        parentPath.register(watcher, StandardWatchEventKinds.ENTRY_MODIFY);

                        // WatcherThread(ThreadGroup threadGroup, String threadName, WatchService watcher) {
                        watcherThread = new WatcherThread(threadGroup, parentDir, watcher);
                        threadIds.put(parentDir, watcherThread.getId());
                        watcherThread.start();
                    }
                }
            }

            watcherThread.regist(fileName, listener);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void destroy(File file) {
        if (file == null) {
            return;
        }

        Path path = file.toPath();
        WatcherThread watchThread = getThread(path.getParent().toAbsolutePath().toString(), true);
        if (watchThread != null) {
            watchThread.remove(path.getFileName().toString());
        }
    }

    private WatcherThread getThread(String dirPath, boolean remove) {
        Long id = (remove) ? threadIds.remove(dirPath) : threadIds.get(dirPath);

        if (id == null) {
            return null;
        }

        Thread[] activeThreads = new Thread[threadGroup.activeCount()];
        threadGroup.enumerate(activeThreads);

        for (Thread t : activeThreads) {
            if (id.longValue() == t.getId()) {
                return (WatcherThread) t;
            }
        }

        return null;
    }

    private void initialize() {
        threadGroup = new ThreadGroup("FILE-WATCHER-THREADS");
        threadGroup.setDaemon(true);
        threadIds = new ConcurrentHashMap<>();

        // TEMP
        Thread threadCheckTask = new Thread(() -> {
            try {
                while (!Thread.currentThread().isInterrupted()) {
                    System.out.println("// ---------------------------------------------------");
                    System.out.println("Check threads");
                    System.out.println("Active thread count : " + threadGroup.activeCount());

                    Thread[] activeThreads = new Thread[threadGroup.activeCount()];
                    threadGroup.enumerate(activeThreads);

                    for (Thread activeThread : activeThreads) {
                        System.out.println(String.format("%s -- %s", activeThread.getId(), activeThread.getName()));
                    }

                    System.out.println("------------------------------------------------------ //");
                    TimeUnit.SECONDS.sleep(5);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        threadCheckTask.setDaemon(true);
        threadCheckTask.start();
        // -- TEMP
    }

    private static class WatcherThread extends Thread {

        private WatchService watcher;
        private Map<String, FileModifiedListener> fileNameListeners;

        public WatcherThread(ThreadGroup threadGroup, String threadName, WatchService watcher) {
            super(threadGroup, threadName);
            this.watcher = watcher;
            this.fileNameListeners = new HashMap<>();
        }

        public void regist(String fileName, FileModifiedListener listener) {
            fileNameListeners.put(fileName, listener);
        }

        public void remove(String fileName) {
            fileNameListeners.remove(fileName);

            if (fileNameListeners.isEmpty()) {
                try {
                    watcher.close();
                } catch(IOException e) {
                    e.printStackTrace();
                    this.interrupt();
                }
            }
        }

        public int filterFileSize() {
            return fileNameListeners.size();
        }

        public void run() {
            try {
                while (!isInterrupted()) {
                    WatchKey watchKey = watcher.take();
                    watchKey.pollEvents().forEach(event -> {

                        if (event.kind() == StandardWatchEventKinds.ENTRY_MODIFY) {
                            Path context = (Path) event.context();
                            String fileName = context.getFileName().toString();

                            FileModifiedListener listener = fileNameListeners.get(fileName);
                            if (listener != null) {
                                listener.onModified();
                            }
                        }
                    });
                }
            } catch (InterruptedException e) {
                this.interrupt();
            } catch (ClosedWatchServiceException e) {
                // ignore
            }
        }
    }
}

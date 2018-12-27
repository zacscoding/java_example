//package nio.file.watch;
//
//import java.io.File;
//import java.io.IOException;
//import java.nio.file.ClosedWatchServiceException;
//import java.nio.file.FileSystems;
//import java.nio.file.Path;
//import java.nio.file.StandardWatchEventKinds;
//import java.nio.file.WatchEvent;
//import java.nio.file.WatchEvent.Kind;
//import java.nio.file.WatchKey;
//import java.nio.file.WatchService;
//import java.util.Map;
//import java.util.concurrent.ConcurrentHashMap;
//import java.util.concurrent.TimeUnit;
//import org.apache.shiro.util.StringUtils;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
///**
// * @author zacconding
// * @Date 2018-10-08
// * @GitHub : https://github.com/zacscoding
// */
//public class FileWatcher2 {
//
//    public static final FileWatcher2 INSTANCE = new FileWatcher2();
//
//    private static final Logger logger = LoggerFactory.getLogger(FileWatcher2.class);
//    private static final Object LOCK = new Object();
//
//    private ThreadGroup threadGroup;
//    private Map<String, Long> threadIds;
//
//    public FileWatcher2() {
//        initialize();
//    }
//
//    public boolean regist(File file, FileListener fileListener) {
//        if (file == null) {
//            logger.warn("Failed to register detecting file because file is null");
//            return false;
//        }
//
//        Path path = file.toPath();
//        Path detectingPath = null;
//
//        if (file.isDirectory()) {
//            detectingPath = path;
//        } else {
//            detectingPath = path.getParent();
//        }
//
//        try {
//            final String absoluteDirPath = detectingPath.toAbsolutePath().toString();
//            WatcherThread watcherThread = getThread(absoluteDirPath, false);
//
//            if (watcherThread == null) {
//                synchronized (LOCK) {
//                    if ((watcherThread = getThread(absoluteDirPath, false)) == null) {
//                        WatchService watcher = FileSystems.getDefault().newWatchService();
//                        path.register(watcher, StandardWatchEventKinds.ENTRY_CREATE, StandardWatchEventKinds.ENTRY_MODIFY,
//                            StandardWatchEventKinds.ENTRY_DELETE);
//
//                        watcherThread = new WatcherThread(threadGroup, absoluteDirPath, detectingPath, watcher);
//                        threadIds.put(absoluteDirPath, watcherThread.getId());
//                        watcherThread.start();
//                    }
//                }
//            }
//
//            watcherThread.regist(fileListener);
//            return true;
//        } catch (IOException e) {
//            logger.warn("IOException occur while registering file detecting.", e);
//            return false;
//        }
//    }
//
//    /**
//     * remove to subscribe
//     * @param file  detected file or directory
//     * @param id    to distingush thread
//     */
//    public void remove(File file, String id) {
//        if (file != null && StringUtils.hasText(id)) {
//            Path detectingPath = getPathOrParentPath(file);
//            WatcherThread thread = getThread(detectingPath.toAbsolutePath().toString(), true);
//            if (thread != null) {
//                thread.remove(id);
//            }
//        }
//    }
//
//    public boolean isSubscribe(File file) {
//        Path detectingPath = getPathOrParentPath(file);
//        if (detectingPath == null) {
//            return false;
//        }
//
//        return getThread(detectingPath.toAbsolutePath().toString(), false) != null;
//    }
//
//    private Path getPathOrParentPath(File file) {
//        if (file == null) {
//            return null;
//        }
//
//        return (file.isDirectory()) ? file.toPath() : file.toPath().getParent();
//    }
//
//    private WatcherThread getThread(String dirPath, boolean remove) {
//        Long id = (remove) ? threadIds.remove(dirPath) : threadIds.get(dirPath);
//
//        if (id == null) {
//            return null;
//        }
//
//        Thread[] activeThreads = new Thread[threadGroup.activeCount()];
//        threadGroup.enumerate(activeThreads);
//
//        for (Thread t : activeThreads) {
//            if (id.longValue() == t.getId()) {
//                return (WatcherThread) t;
//            }
//        }
//
//        return null;
//    }
//
//
//    private void initialize() {
//        threadGroup = new ThreadGroup("FILE-WATCHER-THREADS");
//        threadGroup.setDaemon(true);
//        threadIds = new ConcurrentHashMap<>();
//
//        // TEMP
//        Thread threadCheckTask = new Thread(() -> {
//            try {
//                while (!Thread.currentThread().isInterrupted()) {
//                    System.out.println("// ---------------------------------------------------");
//                    System.out.println("Check threads");
//                    System.out.println("Active thread count : " + threadGroup.activeCount());
//
//                    Thread[] activeThreads = new Thread[threadGroup.activeCount()];
//                    threadGroup.enumerate(activeThreads);
//
//                    for (Thread activeThread : activeThreads) {
//                        System.out.println(String.format("%s -- %s", activeThread.getId(), activeThread.getName()));
//                    }
//
//                    System.out.println("------------------------------------------------------ //");
//                    TimeUnit.SECONDS.sleep(5);
//                }
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        });
//        threadCheckTask.setDaemon(true);
//        threadCheckTask.start();
//        // -- TEMP
//    }
//
//
//    private static class WatcherThread extends Thread {
//
//        private WatchService watcher;
//        private Path path;
//        private Map<String, FileListener> listeners;
//
//        public WatcherThread(ThreadGroup threadGroup, String threadName, Path path, WatchService watcher) {
//            super(threadGroup, threadName);
//            this.watcher = watcher;
//            this.path = path;
//            this.listeners = new ConcurrentHashMap<>();
//        }
//
//        public FileListener regist(FileListener fileListener) {
//            return listeners.put(fileListener.getIdentifier(), fileListener);
//        }
//
//        public boolean isListen(String id) {
//            return listeners.containsKey(id);
//        }
//
//        public void remove(FileListener listener) {
//            if (listener != null) {
//                remove(listener.getIdentifier());
//            }
//        }
//
//        public void remove(String id) {
//            listeners.remove(id);
//            checkAndTerminate();
//
//        }
//
//        private void checkAndTerminate() {
//            if (listeners.isEmpty()) {
//                try {
//                    watcher.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                    this.interrupt();
//                }
//            }
//        }
//
//        public void run() {
//            try {
//                while (!isInterrupted()) {
//                    WatchKey watchKey = watcher.take();
//
//                    for (final WatchEvent<?> event : watchKey.pollEvents()) {
//                        final Kind<?> eventType = event.kind();
//                        final Path context = path.resolve((Path) event.context());
//
//                        listeners.values().forEach(listener -> {
//                            if (listener.isHandle(eventType)) {
//                                listener.handleEvent(eventType, context);
//                            }
//                        });
//                    }
//
//                    watchKey.reset();
//                }
//            } catch (InterruptedException e) {
//                this.interrupt();
//            } catch (ClosedWatchServiceException e) {
//                // ignore
//            }
//        }
//    }
//}

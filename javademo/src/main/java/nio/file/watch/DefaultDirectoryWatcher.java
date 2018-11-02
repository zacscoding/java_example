package nio.file.watch;

import java.io.IOException;
import java.nio.file.ClosedWatchServiceException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchEvent.Kind;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author zacconding
 * @Date 2018-11-01
 * @GitHub : https://github.com/zacscoding
 */
public class DefaultDirectoryWatcher implements DirectoryWatcher {

    private static final Logger logger = LoggerFactory.getLogger(DefaultDirectoryWatcher.class);

    private WatchService watcher;
    // key : WatchKey | value : (Path, DirectoryEventListener)
    private Map<WatchKey, ListenerEntity> eventListeners;
    private Thread watcherTask;

    @Override
    public void start() {
        try {
            stop();

            watcher = FileSystems.getDefault().newWatchService();
            if (eventListeners == null) {
                eventListeners = new ConcurrentHashMap<>();
            }

            watcherTask = new Thread(() -> {
                try {
                    while (!Thread.currentThread().isInterrupted()) {
                        WatchKey watchKey = watcher.take();

                        ListenerEntity pathListenerEntity = eventListeners.get(watchKey);
                        if (pathListenerEntity == null) {
                            continue;
                        }

                        DirectoryEventListener listener = pathListenerEntity.getListener();
                        if (listener == null) {
                            continue;
                        }

                        for (final WatchEvent<?> event : watchKey.pollEvents()) {
                            Kind<?> eventType = event.kind();

                            Path registeredPath = pathListenerEntity.getPath();
                            Path resolvedPath = registeredPath.resolve((Path) event.context());

                            if (eventType == StandardWatchEventKinds.ENTRY_MODIFY) {
                                listener.onModify(resolvedPath);
                            } else if (eventType == StandardWatchEventKinds.ENTRY_CREATE) {
                                listener.onCreate(resolvedPath);
                            } else if (eventType == StandardWatchEventKinds.ENTRY_DELETE) {
                                listener.onDelete(resolvedPath);
                            } else {
                                logger.warn("Failed to handle event. type : " + eventType.toString());
                            }

                            if (!watchKey.reset()) {
                                logger.warn("Failed to watchKey reset.");
                                break;
                            }
                        }
                    }
                } catch (ClosedWatchServiceException e) {
                    return;
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });

            watcherTask.setDaemon(true);
            watcherTask.start();

        } catch (IOException e) {
            logger.warn("Failed to create watch service", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void stop() {
        try {
            if (watcher != null) {
                watcher.close();
            }

            if (eventListeners != null && !eventListeners.isEmpty()) {
                eventListeners.clear();
            }

            if (watcherTask != null) {
                if (!watcherTask.isInterrupted()) {
                    watcherTask.interrupt();
                }
            }
        } catch (Exception e) {
            logger.warn("Failed to stop DirectoryWatcher", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void subscribe(Path path, DirectoryEventListener listener) {
        if (isSubscribe(path)) {
            logger.warn("Already subscribe path. path : " + path.toString());
            return;
        }

        if (!path.toFile().isDirectory()) {
            logger.warn(String.format("Failed to subscribe path. because %s is not directory", path.toAbsolutePath().toString()));
            return;
        }

        try {
            WatchKey key = path.register(watcher, listener.getEventTypes());
            eventListeners.put(key, new ListenerEntity(path, listener));
        } catch (IOException e) {
            logger.warn("IOException occur while registering path subscription", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean isSubscribe(Path path) {
        if (path == null) {
            return false;
        }

        for (Entry<WatchKey, ListenerEntity> entry : eventListeners.entrySet()) {
            if (path.equals(entry.getValue().getPath())) {
                return true;
            }
        }

        return false;
    }

    @Override
    public void unsubscribe(Path path) {
        if (path == null) {
            return;
        }

        for (Entry<WatchKey, ListenerEntity> entry : eventListeners.entrySet()) {
            if (path.equals(entry.getValue().getPath())) {
                eventListeners.remove(entry.getKey());
                return;
            }
        }
    }

    private static class ListenerEntity {

        private Path path;
        private DirectoryEventListener listener;

        public ListenerEntity(Path path, DirectoryEventListener listener) {
            this.path = path;
            this.listener = listener;
        }

        public Path getPath() {
            return path;
        }

        public void setPath(Path path) {
            this.path = path;
        }

        public DirectoryEventListener getListener() {
            return listener;
        }

        public void setListener(DirectoryEventListener listener) {
            this.listener = listener;
        }
    }
}

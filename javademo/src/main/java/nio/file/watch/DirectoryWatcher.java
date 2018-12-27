package nio.file.watch;

import java.nio.file.Path;

/**
 * Directory event watcher
 *
 * @author zacconding
 * @Date 2018-11-01
 * @GitHub : https://github.com/zacscoding
 */
public interface DirectoryWatcher {

    /**
     * Watcher start
     */
    void start();

    /**
     * Watcher stop all
     */
    void stop();

    /**
     * subscribe event
     *
     * @param path     : directory path
     * @param listener : event listener
     */
    void subscribe(Path path, DirectoryEventListener listener);

    /**
     * check whether subscribe or not
     */
    boolean isSubscribe(Path path);

    /**
     * unsubscribe
     */
    void unsubscribe(Path path);
}
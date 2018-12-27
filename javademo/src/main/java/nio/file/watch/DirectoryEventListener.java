package nio.file.watch;

import java.nio.file.Path;
import java.nio.file.WatchEvent.Kind;

/**
 * Directory modify event
 *
 * @author zacconding
 * @Date 2018-11-01
 * @GitHub : https://github.com/zacscoding
 */
public interface DirectoryEventListener {

    /**
     * handle event types
     */
    Kind<?>[] getEventTypes();

    /**
     * Create event
     */
    void onCreate(Path eventPath);

    /**
     * Modify event
     */
    void onModify(Path eventPath);

    /**
     * Delete event
     */
    void onDelete(Path eventPath);
}

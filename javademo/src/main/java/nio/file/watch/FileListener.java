package nio.file.watch;

import java.nio.file.Path;
import java.nio.file.WatchEvent.Kind;

/**
 * @author zacconding
 * @Date 2018-10-08
 * @GitHub : https://github.com/zacscoding
 */
public interface FileListener {

    String getIdentifier();

    boolean isHandle(Kind<?> eventType);

    void handleEvent(Kind<?> eventType, Path context);
}
package nio.file;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent.Kind;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import nio.file.watch.FileListener;
import nio.file.watch.FileWatcher2;
import org.junit.Test;
import util.SimpleLogger;

/**
 * @author zacconding
 * @Date 2018-10-08
 * @GitHub : https://github.com/zacscoding
 */
public class FileWatcher2Test {

    @Test
    public void consoleDirectory() throws Exception {
        File tempDir = new File("F:\\testdir");
        FileListener listener = new FileListener() {
            @Override
            public String getIdentifier() {
                return "all";
            }

            @Override
            public boolean isHandle(Kind<?> eventType) {
                if (eventType == StandardWatchEventKinds.ENTRY_MODIFY || eventType == StandardWatchEventKinds.ENTRY_CREATE) {
                    return true;
                }

                return false;
            }

            @Override
            public void handleEvent(Kind<?> eventType, Path context) {
                SimpleLogger.println("Handle tempDir event..");
            }
        };

        System.out.println(">> Try to regist..");
        FileWatcher2.INSTANCE.regist(tempDir, listener);
        TimeUnit.SECONDS.sleep(5);
        System.out.println(">> Try to remove..");
        FileWatcher2.INSTANCE.remove(tempDir, "all");
        TimeUnit.MINUTES.sleep(1);
    }

    @Test
    public void consoleFileTest() throws Exception {
        File file = new File("F:\\test\\read-write-test.txt");
        FileListener listener = new CommonFileListener(file);

        System.out.println(">> Register");
        FileWatcher2.INSTANCE.regist(file, listener);
        TimeUnit.SECONDS.sleep(20L);

        System.out.println(">> Removed");
        FileWatcher2.INSTANCE.remove(file, file.getName());
        TimeUnit.SECONDS.sleep(20L);
    }

    public static class CommonFileListener implements FileListener {

        private File file;
        private String fileName;
        private long lastModifieid = -1L;

        private CommonFileListener(File file) {
            Objects.requireNonNull(file, "file must be not null");
            this.file = file;
            this.fileName = file.getName();
        }

        @Override
        public String getIdentifier() {
            return file.getName();
        }

        @Override
        public boolean isHandle(Kind<?> eventType) {
            return eventType == StandardWatchEventKinds.ENTRY_MODIFY;
        }

        @Override
        public void handleEvent(Kind<?> eventType, Path context) {
            if (!context.getFileName().equals(file.getName())) {
                return;
            }

            long fileModified = context.toFile().lastModified();

            if (fileModified <= lastModifieid) {
                return;
            }

            lastModifieid = fileModified;
            System.out.println("File is modified!!!");
        }
    }
}

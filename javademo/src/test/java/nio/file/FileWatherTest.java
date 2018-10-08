package nio.file;

import java.io.File;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.Arrays;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import nio.file.watch.FileModifiedListener;
import nio.file.watch.FileWatcher;
import org.junit.Test;
import util.SimpleLogger;

/**
 * @author zacconding
 * @Date 2018-08-04
 * @GitHub : https://github.com/zacscoding
 */
public class FileWatherTest {

    private AtomicInteger events;

    @Test
    public void checkEvent() throws Exception {
        String path = "F:\\testdir";
        Path testPath = Paths.get(path);

        WatchService watcher = FileSystems.getDefault().newWatchService();
        testPath.register(watcher, StandardWatchEventKinds.ENTRY_MODIFY, StandardWatchEventKinds.ENTRY_CREATE, StandardWatchEventKinds.ENTRY_DELETE);

        while (!Thread.currentThread().isInterrupted()) {
            WatchKey watchKey = watcher.take();
            events = new AtomicInteger();

            for (final WatchEvent<?> event : watchKey.pollEvents()) {
                events.getAndIncrement();
                if (event.kind() == StandardWatchEventKinds.ENTRY_MODIFY) {
                    System.out.println("StandardWatchEventKinds.ENTRY_MODIFY occur..");
                } else if (event.kind() == StandardWatchEventKinds.ENTRY_CREATE) {
                    System.out.println("StandardWatchEventKinds.ENTRY_CREATE occur..");
                } else if (event.kind() == StandardWatchEventKinds.ENTRY_DELETE) {
                    System.out.println("StandardWatchEventKinds.ENTRY_DELETE occur..");
                } else {
                    System.out.println("UnknownEventType... " + event.kind());
                }

                final Path context = testPath.resolve((Path) event.context());
                String fileName = context.getFileName().toString();
                boolean isDirectory = context.toFile().isDirectory();

                SimpleLogger.println("File name : {}, type : {} , last modified : {}"
                    ,fileName, (isDirectory ? "directory" : "file"), context.toFile().lastModified());

                if (!watchKey.reset()) {
                    return;
                }
            }
            System.out.println("Event count .. :: " + events.get());
        }
    }


    @Test
    public void test() throws Exception {
        CountDownLatch countDownLatch = new CountDownLatch(3);

        Path testDirPath = Paths.get("src/test/resources/nio-file-test");
        File testDir = testDirPath.toFile();
        File testDir1 = new File(testDir, "test1");
        File testDir2 = new File(testDir, "test2");

        File f1 = new File(testDir1, "test1.txt");
        File f11 = new File(testDir1, "test2.txt");
        File f2 = new File(testDir2, "test2.txt");

        FileWatcher fileWatcher = FileWatcher.INSTANCE;
        fileWatcher.regist(f1, getListener(f1.getName()));
        fileWatcher.regist(f2, getListener(f2.getName()));
        fileWatcher.regist(f11, getListener(f11.getName()));

        TimeUnit.SECONDS.sleep(5);

        fileWatcher.destroy(f2);

        TimeUnit.MINUTES.sleep(1);
    }

    public FileModifiedListener getListener(String fileName) {
        return new FileModifiedListener() {
            @Override
            public void onModified() {
                System.out.println("## Modified file : " + fileName);
            }
        };
    }
}

package nio.file;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import nio.file.watch.FileModifiedListener;
import nio.file.watch.FileWatcher;
import org.junit.Test;

/**
 * @author zacconding
 * @Date 2018-08-04
 * @GitHub : https://github.com/zacscoding
 */
public class FileWatherTest {


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

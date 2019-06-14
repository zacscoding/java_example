package nio.file;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent.Kind;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import nio.file.watch.DefaultDirectoryWatcher;
import nio.file.watch.DirectoryEventListener;
import nio.file.watch.DirectoryWatcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author zacconding
 */
public class DirectoryWatcherTest {

    DirectoryWatcher watcher;
    File rootFile;

    @Before
    public void setUp() {
        watcher = new DefaultDirectoryWatcher();
        watcher.start();
        rootFile = new File("src/test/resources/directory-watcher");
        rm(rootFile);
        rootFile.mkdirs();
    }

    @After
    public void tearDown() {
        watcher.stop();
        rm(rootFile);
    }

    @Test
    public void subscribe() throws IOException, InterruptedException {
        Path testDir = rootFile.toPath();

        Path child1 = testDir.resolve("child1");
        child1.toFile().mkdirs();
        Path child2 = testDir.resolve("child2");
        child2.toFile().mkdirs();

        CountDownLatch countDownLatch = new CountDownLatch(3);
        TestDirectoryEventListener listener = new TestDirectoryEventListener(
            new Kind<?>[]{StandardWatchEventKinds.ENTRY_CREATE}, countDownLatch);

        watcher.subscribe(child1, listener);
        watcher.subscribe(child2, listener);

        assertTrue(watcher.isSubscribe(child1));
        assertTrue(watcher.isSubscribe(child2));

        child1.resolve("child11").toFile().mkdirs();
        child1.resolve("child12").toFile().mkdirs();
        child2.resolve("child21").toFile().mkdirs();

        assertTrue(countDownLatch.await(3000L, TimeUnit.MILLISECONDS));

        assertTrue(listener.getCreateCount() == 3);
    }

    @Test
    public void unsubscribe() throws InterruptedException {
        Path testDir = rootFile.toPath();

        Path child1 = testDir.resolve("child1");
        child1.toFile().mkdirs();

        CountDownLatch countDownLatch = new CountDownLatch(1);
        TestDirectoryEventListener listener = new TestDirectoryEventListener(
            new Kind<?>[]{StandardWatchEventKinds.ENTRY_CREATE}, countDownLatch);

        watcher.subscribe(child1, listener);

        child1.resolve("child11").toFile().mkdirs();

        assertTrue(countDownLatch.await(3000L, TimeUnit.MILLISECONDS));
        assertTrue(listener.getCreateCount() == 1);

        watcher.unsubscribe(child1);

        child1.resolve("child11").toFile().mkdirs();
        assertTrue(listener.getCreateCount() == 1);
    }

    private static class TestDirectoryEventListener implements DirectoryEventListener {

        private Kind<?>[] types;
        private CountDownLatch countDownLatch;
        private AtomicInteger createCount;
        private AtomicInteger modifyCount;
        private AtomicInteger deleteCount;

        public TestDirectoryEventListener(Kind<?>[] types, CountDownLatch countDownLatch) {
            this.types = types;
            this.countDownLatch = countDownLatch;
            this.createCount = new AtomicInteger(0);
            this.modifyCount = new AtomicInteger(0);
            this.deleteCount = new AtomicInteger(0);
        }

        @Override
        public Kind<?>[] getEventTypes() {
            return types;
        }

        @Override
        public void onCreate(Path eventPath) {
            System.out.println("onCreate... " + eventPath.toAbsolutePath().toString());
            createCount.incrementAndGet();
            countDownLatch.countDown();
        }

        @Override
        public void onModify(Path eventPath) {
            System.out.println("onModify... " + eventPath.toAbsolutePath().toString());
            modifyCount.incrementAndGet();
            countDownLatch.countDown();
        }

        @Override
        public void onDelete(Path eventPath) {
            System.out.println("onDelete... " + eventPath.toAbsolutePath().toString());
            deleteCount.incrementAndGet();
            countDownLatch.countDown();
        }

        public int getCreateCount() {
            return createCount.get();
        }

        public int getModifyCount() {
            return modifyCount.get();
        }

        public int getDeleteCount() {
            return deleteCount.get();
        }
    }

    private void rm(File file) {
        if (file == null) {
            return;
        }

        if (file.isDirectory()) {
            for (File child : file.listFiles()) {
                rm(child);
            }
        }

        file.delete();
    }
}

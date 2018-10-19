package nio.file;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import org.junit.Test;
import util.SimpleLogger;

/**
 * @author zacconding
 * @Date 2018-06-27
 * @GitHub : https://github.com/zacscoding
 */
public class NioFileBasicTest {

    /**
     * source code : http://palpit.tistory.com/640
     */
    @Test
    public void path() throws Exception {
        /*
        path.toString() : C:\git\java_example\javademo\target\test-classes\temp.txt
        path.getFileName() : temp.txt
        path.getParent().getFileName() : test-classes
        path.getNameCount() : 6
        0 : git
        1 : java_example
        2 : javademo
        3 : target
        4 : test-classes
        5 : temp.txt
        git
        java_example
        javademo
        target
        test-classes
        temp.txt
         */
        URL url = NioFileBasicTest.class.getClassLoader().getResource("temp.txt");
        String filePath = url.getPath().substring(1); // for window

        Path path = Paths.get(filePath);
        SimpleLogger.build().appendln("path.toString() : " + path.toString()).appendln("path.getFileName() : " + path.getFileName())
                    .appendln("path.getParent().getFileName() : " + path.getParent().getFileName()).appendln("path.getNameCount() : " + path.getNameCount()).flush();

        for (int i = 0; i < path.getNameCount(); i++) {
            System.out.println(i + " : " + path.getName(i));
        }

        path.iterator().forEachRemaining(path1 -> System.out.println(path1.toString()));
    }

    @Test
    public void watchTest() throws Exception {
        Path path = Paths.get("src/test/resources/nio-file-test");
        if (Files.exists(path)) {
            File root = path.toFile();
            for (File file : root.listFiles()) {
                System.out.println("remove file : " + file.delete());
            }
        } else {
            if (!Files.exists(path)) {
                Files.createDirectory(path);
            }
        }

        WatchService watchService = FileSystems.getDefault().newWatchService();
        Random random = new Random();
        path.register(watchService, StandardWatchEventKinds.ENTRY_CREATE, StandardWatchEventKinds.ENTRY_MODIFY, StandardWatchEventKinds.ENTRY_DELETE);

        new Thread(() -> {
            try {
                while (!Thread.currentThread().isInterrupted()) {
                    WatchKey key = watchService.take();

                    key.pollEvents().forEach(event -> {
                        Path eventCtx = (Path) event.context();
                        SimpleLogger.println("[Watch] event.kind() => name : {}, class : {}, context class : {}, path : {}", event.kind().name(), event.kind().type().getName(),
                            event.context().getClass().getName(), eventCtx.toString());
                    });

                    if (!key.reset()) {
                        break;
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();

        new Thread(() -> {
            try {
                int count = 10;
                for (int i = 0; i <= count; i++) {
                    Path filePath = Paths.get(path.toString(), "file" + i + ".txt");
                    Files.createFile(filePath);
                }

                while (!Thread.currentThread().isInterrupted()) {
                    ++count;
                    // 0 : create , 1 : modify , 3 : delete
                    int type = random.nextInt(3);
                    if (type == 0) {
                        System.out.println("[Task] create file");
                        Path filePath = Paths.get(path.toString(), "file" + count + ".txt");
                        Files.createFile(filePath);
                    } else if (type == 1) {
                        File dir = path.toFile();
                        if (dir.listFiles().length != 0) {
                            System.out.println("[Task] modify file");
                            File modifyFile = dir.listFiles()[random.nextInt(dir.listFiles().length)];
                            try (FileOutputStream fos = new FileOutputStream(modifyFile)) {
                                fos.write(new byte[] {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0});
                            }
                        }
                    } else if (type == 2) {
                        File dir = path.toFile();
                        if (dir.listFiles().length != 0) {
                            System.out.println("[Task] remove file");
                            File removeFile = dir.listFiles()[random.nextInt(dir.listFiles().length)];
                            removeFile.delete();
                        }
                    } else {
                        System.out.println("type is error : " + type);
                    }
                    TimeUnit.SECONDS.sleep(1L);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();

        TimeUnit.SECONDS.sleep(30L);
    }

    @Test
    public void eventRecursive() throws Exception {
        Path path = Paths.get("src/test/resources/nio-file-test");
        if (Files.exists(path)) {
            File root = path.toFile();
            for (File file : root.listFiles()) {
                System.out.println("remove file : " + file.delete());
            }
        } else {
            if (!Files.exists(path)) {
                Files.createDirectory(path);
            }
        }

        WatchService watchService = FileSystems.getDefault().newWatchService();
        path.register(watchService, StandardWatchEventKinds.ENTRY_CREATE, StandardWatchEventKinds.ENTRY_MODIFY, StandardWatchEventKinds.ENTRY_DELETE);

        Thread t = new Thread(() -> {
            try {
                while (!Thread.currentThread().isInterrupted()) {
                    WatchKey key = watchService.take();

                    key.pollEvents().forEach(event -> {
                        Path eventCtx = (Path) event.context();
                        SimpleLogger.println("[Watch] event.kind() => name : {}, class : {}, context class : {}, path : {}", event.kind().name(), event.kind().type().getName(),
                            event.context().getClass().getName(), eventCtx.toString());
                    });

                    if (!key.reset()) {
                        break;
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        t.setDaemon(true);
        t.start();

        TimeUnit.MINUTES.sleep(2);
    }


    @Test
    public void fileDetectTest() throws Exception {
        Path path = Paths.get("src/test/resources/nio-file-test");
        /*boolean clear = false;
        if (Files.exists(path)) {
            File root = path.toFile();
            clear = root.delete();
        } else {
            clear = true;
        }
        if(clear) {
            Files.createDirectory(path);
        }*/

        WatchService watchService = FileSystems.getDefault().newWatchService();
        path.register(watchService, StandardWatchEventKinds.ENTRY_CREATE, StandardWatchEventKinds.ENTRY_MODIFY, StandardWatchEventKinds.ENTRY_DELETE);

        CountDownLatch countDownLatch = new CountDownLatch(3);

        Thread t = new Thread(() -> {
            try {
                while (!Thread.currentThread().isInterrupted()) {
                    if (countDownLatch.getCount() == 0) {
                        break;
                    }

                    WatchKey key = watchService.take();

                    key.pollEvents().forEach(event -> {
                        Path eventCtx = (Path) event.context();
                        SimpleLogger.println("[Watch] event.kind() => name : {}, class : {}, context class : {}, toAbsolutePath : {}, fileName : {}", event.kind().name(), event.kind().type().getName(),
                            event.context().getClass().getName(), eventCtx.toAbsolutePath(), eventCtx.getFileName().toString());
                    });

                    if (!key.reset()) {
                        break;
                    }

                    countDownLatch.countDown();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        t.setDaemon(true);
        t.start();

        /*File dir = path.toFile();

        // crate dir
        File newDir = new File(dir, "new-dir");
        newDir.mkdirs();

        // crate dir
        File newDir2 = new File(dir, "new-dir2");
        newDir2.mkdirs();

        // create file
        File newFile = new File(newDir, "test.txt");
        newFile.createNewFile();*/

        countDownLatch.await(2, TimeUnit.MINUTES);
    }

    @Test
    public void multiplePathWatcher() throws IOException, InterruptedException {
        Path path1 = Paths.get("F:\\test\\path1");
        Path path2 = Paths.get("F:\\test\\path2");

        WatchService watchService = FileSystems.getDefault().newWatchService();
        path1.register(watchService, StandardWatchEventKinds.ENTRY_CREATE, StandardWatchEventKinds.ENTRY_MODIFY, StandardWatchEventKinds.ENTRY_DELETE);
        path2.register(watchService, StandardWatchEventKinds.ENTRY_CREATE, StandardWatchEventKinds.ENTRY_MODIFY, StandardWatchEventKinds.ENTRY_DELETE);

        CountDownLatch countDownLatch = new CountDownLatch(3);
        Thread t = new Thread(() -> {
            try {
                while (!Thread.currentThread().isInterrupted()) {
                    if (countDownLatch.getCount() == 0) {
                        break;
                    }

                    WatchKey key = watchService.take();

                    for (WatchEvent<?> event : key.pollEvents()) {
                        Path eventCtx = ((WatchEvent<Path>)event).context();
                        Path resolvedPath = path1.resolve(eventCtx);
                        SimpleLogger.println("Evfent occur..\neventCtx.toFile().toString() : {}", eventCtx.toFile().toString());
                    }

                    if (!key.reset()) {
                        break;
                    }

                    countDownLatch.countDown();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        t.setDaemon(true);
        t.start();

        countDownLatch.await();
        t.interrupt();
        System.out.println(">> Complete");
    }

    @Test
    public void kindTest() {

    }

    @Test
    public void pathHashCode() {
        Path p1 = Paths.get("F:\\test\\path1");
        Path p2 = Paths.get("F:\\test\\path1");

        System.out.println("addr : " + p1.toString() + " , " + p2.toString());
        System.out.println("hash : " + p1.hashCode() + " , " + p2.hashCode());
        System.out.println(p1.equals(p2));
    }
}

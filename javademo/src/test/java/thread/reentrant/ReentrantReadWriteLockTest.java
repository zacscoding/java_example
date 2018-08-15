package thread.reentrant;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;
import org.junit.Before;
import org.junit.Test;
import util.SimpleLogger;

/**
 * @author zacconding
 * @Date 2018-08-15
 * @GitHub : https://github.com/zacscoding
 */
public class ReentrantReadWriteLockTest {

    Path filePath = Paths.get("F:\\test\\read-write-test.txt");
    File file;
    ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    CountDownLatch countDownLatch = new CountDownLatch(5);

    @Before
    public void setUp() throws Exception {
        File dir = filePath.getParent().toFile();
        if (!dir.canRead()) {
            dir.mkdirs();
        }

        if (!dir.canRead()) {
            throw new RuntimeException("Can`t read " + dir.getAbsolutePath());
        }

        file = filePath.toFile();

        if (file.exists()) {
            file.delete();
        }

        file.createNewFile();

        if (!file.exists()) {
            throw new RuntimeException("Can`t create file : " + file.getAbsolutePath());
        }
    }

    /*
------------------------------------------------------------
> Try to write :: [Write Thread THREAD-1 - Thread-1 | Repeat : 2 | Count : 5
------------------------------------------------------------

------------------------------------------------------------
> Try to write :: [Write Thread THREAD-2 - Thread-2 | Repeat : 1 | Count : 4
------------------------------------------------------------

------------------------------------------------------------
> Try to read.
>> [Read Thread - Thread-0]
Write content in [THREAD-1] :: 0
Write content in [THREAD-1] :: 1
Write content in [THREAD-2] :: 0
------------------------------------------------------------

------------------------------------------------------------
> Try to read.
>> [Read Thread - Thread-0]
Write content in [THREAD-1] :: 0
Write content in [THREAD-1] :: 1
Write content in [THREAD-2] :: 0
------------------------------------------------------------

------------------------------------------------------------
> Try to write :: [Write Thread THREAD-2 - Thread-2 | Repeat : 1 | Count : 3
------------------------------------------------------------

------------------------------------------------------------
> Try to write :: [Write Thread THREAD-1 - Thread-1 | Repeat : 2 | Count : 2
------------------------------------------------------------

------------------------------------------------------------
> Try to read.
>> [Read Thread - Thread-0]
Write content in [THREAD-1] :: 0
Write content in [THREAD-1] :: 1
Write content in [THREAD-2] :: 0
Write content in [THREAD-2] :: 1
Write content in [THREAD-1] :: 2
Write content in [THREAD-1] :: 3
------------------------------------------------------------

------------------------------------------------------------
> Try to write :: [Write Thread THREAD-2 - Thread-2 | Repeat : 2 | Count : 1
------------------------------------------------------------

------------------------------------------------------------
------------------------------------------------------------
> Try to write :: [Write Thread THREAD-1 - Thread-1 | Repeat : 3 | Count : 0
> final file
Write content in [THREAD-1] :: 0
Write content in [THREAD-1] :: 1
Write content in [THREAD-2] :: 0
Write content in [THREAD-2] :: 1
Write content in [THREAD-1] :: 2
Write content in [THREAD-1] :: 3
Write content in [THREAD-2] :: 2
Write content in [THREAD-2] :: 3
     */

    @Test
    public void readAndWrite() throws Exception {
        Thread readThread = new Thread(readRunnable());
        Thread writeThread1 = new Thread(writeRunnable("THREAD-1"));
        Thread writeThread2 = new Thread(writeRunnable("THREAD-2"));

        readThread.setDaemon(true);
        writeThread1.setDaemon(true);
        writeThread2.setDaemon(true);

        readThread.start();
        writeThread1.start();
        writeThread2.start();

        countDownLatch.await();

        SimpleLogger.println("------------------------------------------------------------");
        SimpleLogger.println("> final file\n{}", Files.readAllLines(filePath).stream().collect(Collectors.joining("\n")));
    }

    public Runnable readRunnable() {
        return () -> {
            String prefix = "[Read Thread - " + Thread.currentThread().getName() + "]";
            while (!Thread.currentThread().isInterrupted()) {
                if (countDownLatch.getCount() <= 1L) {
                    return;
                }

                try {
                    lock.readLock().lock();
                    SimpleLogger.println("------------------------------------------------------------");
                    SimpleLogger.println("> Try to read.");
                    String content = Files.readAllLines(filePath).stream().collect(Collectors.joining("\n"));
                    SimpleLogger.println(">> {}\n{}", prefix, content);
                    TimeUnit.SECONDS.sleep(1L);
                    SimpleLogger.println("------------------------------------------------------------\n");
                } catch (Exception e) {
                    SimpleLogger.error("Exception occur while read content", e);
                    break;
                } finally {
                    lock.readLock().unlock();
                }
            }
        };
    }

    public Runnable writeRunnable(final String name) {
        return () -> {
            String threadName = "[Write Thread " + name + " - " + Thread.currentThread().getName();
            int seq = 0;
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    lock.writeLock().lock();
                    int repeat = new Random().nextInt(3) + 1;
                    SimpleLogger.println("------------------------------------------------------------");
                    SimpleLogger.println("> Try to write :: {} | Repeat : {} | Count : {}", threadName, repeat, countDownLatch.getCount());
                    PrintStream ps = new PrintStream(new FileOutputStream(file, true), true);
                    for (int i = 0; i < repeat; i++) {
                        ps.printf("Write content in [%s] :: %d", name, seq++);
                        ps.println();
                        TimeUnit.SECONDS.sleep(1L);
                    }
                } catch (Exception e) {
                    SimpleLogger.error("Exception occur while writing file", e);
                    break;
                } finally {
                    SimpleLogger.println("------------------------------------------------------------\n");
                    lock.writeLock().unlock();
                    countDownLatch.countDown();
                }

                try {
                    long sleepTime = new Random().nextInt(3) + 1;
                    TimeUnit.SECONDS.sleep(sleepTime);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        };
    }
}
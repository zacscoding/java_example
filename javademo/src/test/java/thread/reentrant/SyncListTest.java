package thread.reentrant;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;
import org.junit.Before;
import org.junit.Test;

/**
 * @author zacconding
 * @Date 2018-09-06
 * @GitHub : https://github.com/zacscoding
 */
public class SyncListTest {

    List<String> names;
    ReentrantReadWriteLock lock;

    @Before
    public void setUp() {
        names = new LinkedList<>();
        lock = new ReentrantReadWriteLock();
    }

    @Test
    public void temp() throws Exception {
        Random random = new Random();
        final int bound = 'z' - 'a';
        final int start = 'a';
        CountDownLatch countDownLatch = new CountDownLatch(100);
        Runnable writeRunnable = () -> {
            try {
                TimeUnit.MILLISECONDS.sleep(random.nextInt(1000));
                char ch = (char) (random.nextInt(bound) + start);
                write(String.valueOf(ch));
                read(true);
                countDownLatch.countDown();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        };
        Runnable readRunnable = () -> {
            try {
                TimeUnit.MILLISECONDS.sleep(random.nextInt(1000));
                read(false);
                countDownLatch.countDown();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        };
        Runnable deleteRunnable = () -> {
            try {
                TimeUnit.MILLISECONDS.sleep(random.nextInt(1000));
                char ch = (char) (random.nextInt(bound) + start);
                delete(String.valueOf(ch));
                read(true);
                countDownLatch.countDown();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        };

        Runnable[] runnables = new Runnable[] {writeRunnable, readRunnable, deleteRunnable};

        ExecutorService executorService = Executors.newFixedThreadPool(100);
        for (int i = 0; i < 100; i++) {
            int idx = random.nextInt(runnables.length);
            executorService.submit(runnables[idx]);
        }
        countDownLatch.await();
        System.out.println("-----------------------------------------------");
        read(false);
    }

    public void read(boolean sequence) {
        try {
            lock.readLock().lock();
            String readNames = names.stream().collect(Collectors.joining(" "));
            if (sequence) {
                System.out.println(" >>> result : " + readNames);
            } else {
                System.out.println("read all >> " + readNames);
            }
        } finally {
            lock.readLock().unlock();
        }
    }

    public void write(String name) {
        try {
            lock.writeLock().lock();
            names.add(name);
            System.out.print("write >> " + name);
        } finally {
            lock.writeLock().unlock();
        }
    }

    public void delete(String name) {
        try {
            lock.writeLock().lock();
            for (int i = 0; i < names.size(); i++) {
                if (name.equals(names.get(i))) {
                    names.remove(i);
                    i--;
                }
            }
            System.out.print("remove >> " + name);
        } finally {
            lock.writeLock().unlock();
        }
    }
}

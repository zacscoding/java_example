package cache.guava;

import static org.junit.Assert.assertTrue;

import com.codahale.metrics.Counter;
import java.util.concurrent.TimeUnit;
import org.junit.Test;

/**
 * @GitHub : https://github.com/zacscoding
 */
public class SingleLoadingCacheTest {

    @Test
    public void temp() throws InterruptedException {
        final Counter counter = new Counter();
        SingleLoadingCache<SingleLoadingCacheTestEntity> cache = new SingleLoadingCache(
            1, 2, TimeUnit.SECONDS, () -> new SingleLoadingCacheTestEntity(counter)
        );

        cache.getUnchecked();
        assertTrue(counter.getCount() == 1);
        cache.getUnchecked();
        cache.getUnchecked();
        cache.getUnchecked();
        cache.getUnchecked();
        assertTrue(counter.getCount() == 1);

        TimeUnit.SECONDS.sleep(3L);
        cache.getUnchecked();
        assertTrue(counter.getCount() == 2);
    }

    private static class SingleLoadingCacheTestEntity {

        private Counter counter;

        public SingleLoadingCacheTestEntity(Counter counter) {
            System.out.println("SingleLoadingCacheTestEntity::constructor is called");
            this.counter = counter;
            this.counter.inc();
        }
    }
}

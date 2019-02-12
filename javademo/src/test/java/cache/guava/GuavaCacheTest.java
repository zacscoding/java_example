package cache.guava;

import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import com.codahale.metrics.Counter;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import org.junit.Test;

/**
 * @GitHub : https://github.com/zacscoding
 */
public class GuavaCacheTest {

    @Test
    public void testBasic() throws InterruptedException {
        final Counter counter = new Counter();
        final String key = "key";

        LoadingCache<String, TestCacheValue> cache = CacheBuilder.newBuilder()
            .maximumSize(100)
            .expireAfterWrite(2, TimeUnit.SECONDS)
            .build(
                new CacheLoader<String, TestCacheValue>() {
                    @Override
                    public TestCacheValue load(String key) throws Exception {
                        return new TestCacheValue(key, counter);
                    }
                }
            );

        cache.getUnchecked(key);
        cache.getUnchecked(key);
        cache.getUnchecked(key);
        assertTrue(counter.getCount() == 1);
        TimeUnit.SECONDS.sleep(3L);
        cache.getUnchecked(key);
        assertTrue(counter.getCount() == 2);
    }

    private static class TestCacheValue {

        private String key;
        private Counter counter;

        public TestCacheValue(String key, Counter counter) {
            this.key = key;
            this.counter = counter;
            this.counter.inc();
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }
    }
}

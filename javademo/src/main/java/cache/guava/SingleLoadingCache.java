package cache.guava;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

/**
 * @GitHub : https://github.com/zacscoding
 */
public class SingleLoadingCache<V> {

    private LoadingCache<Object, V> cache;
    private Object singleKey = new Object();

    public SingleLoadingCache(int maximum, long duration, TimeUnit unit, Supplier<V> cacheLoader) {
        this.cache = CacheBuilder.newBuilder()
            .maximumSize(maximum)
            .expireAfterWrite(duration, unit)
            .build(
                new CacheLoader<Object, V>() {
                    @Override
                    public V load(Object key) throws Exception {
                        return cacheLoader.get();
                    }
                }
            );
    }

    public V get() throws ExecutionException {
        return cache.get(singleKey);
    }

    public V getUnchecked() {
        return cache.getUnchecked(singleKey);
    }
}

package demo.leader;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @GitHub : https://github.com/zacscoding
 */
public class LocalCacheLeaderSelector implements LeaderSelector {

    private LoadingCache<String, AtomicInteger> cache;

    public LocalCacheLeaderSelector() {
        this.cache = CacheBuilder.newBuilder()
            .maximumSize(100)
            .expireAfterWrite(5000L, TimeUnit.MILLISECONDS)
            .build(
                new CacheLoader<String, AtomicInteger>() {
                    @Override
                    public AtomicInteger load(String key) {
                        return new AtomicInteger(leaderNumber);
                    }
                }
            );
    }

    public int getJobOrder(String jobId) {
        return cache.getUnchecked(jobId).getAndIncrement();
    }
}
package health;

import com.codahale.metrics.health.HealthCheck;
import com.codahale.metrics.health.HealthCheck.Result;
import com.codahale.metrics.health.HealthCheckRegistry;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Predicate;
import lombok.extern.slf4j.Slf4j;

/**
 * @GitHub : https://github.com/zacscoding
 */
@Slf4j
public class HealthChecker implements Runnable {

    public static HealthChecker INSTANCE = new HealthChecker();

    private ScheduledExecutorService scheduledExecutor;
    private ReentrantReadWriteLock lock;
    private HealthCheckRegistry healthChecks;
    private Map<String, Result> healthStatusMap;

    private HealthChecker() {
        this.lock = new ReentrantReadWriteLock();
        this.healthChecks = new HealthCheckRegistry();
        this.healthStatusMap = new HashMap<>();
    }

    /**
     * Start health checker
     */
    public void start() {
        if (isRunning()) {
            logger.warn("Already health check scheduler is running");
            return;
        }

        scheduledExecutor = Executors.newSingleThreadScheduledExecutor();
        scheduledExecutor.scheduleAtFixedRate(this, 3000L, 3000L, TimeUnit.MILLISECONDS);

        Runtime.getRuntime().addShutdownHook(new Thread(() -> HealthChecker.this.stop()));
    }

    /**
     * Stop health checker and unregister all
     */
    public void stop() {
        if (scheduledExecutor == null) {
            return;
        }

        scheduledExecutor.shutdown();

        for (String name : healthChecks.getNames()) {
            unregister(name);
        }

        healthStatusMap.clear();

        try {
            scheduledExecutor.awaitTermination(3000, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
        }
    }

    /**
     * Check whether health checker is running or not
     */
    public boolean isRunning() {
        return (scheduledExecutor != null)
            && (!scheduledExecutor.isShutdown())
            && (!scheduledExecutor.isTerminated());
    }

    /**
     * Register health check
     */
    public void register(String name, HealthCheck healthCheck) {
        healthChecks.register(name, healthCheck);
    }

    /**
     * Unregister health check
     */
    public void unregister(String name) {
        healthChecks.unregister(name);
    }

    public List<String> getHealthyNodes() {
        return getNodesByStatus(result -> result.isHealthy());
    }

    public List<String> getUnhealthyNodes() {
        return getNodesByStatus(result -> !result.isHealthy());
    }

    private List<String> getNodesByStatus(Predicate<Result> sortedPredicate) {
        try {
            lock.readLock().lock();
            List<String> nodes = new ArrayList<>();
            for (Entry<String, Result> entry : healthStatusMap.entrySet()) {
                if (sortedPredicate.test(entry.getValue())) {
                    nodes.add(entry.getKey());
                }
            }

            return nodes;
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public void run() {
        SortedMap<String, Result> results = healthChecks.runHealthChecks();
        try {
            lock.writeLock().lock();

            for (Entry<String, Result> entry : results.entrySet()) {
                String name = entry.getKey();
                Result result = entry.getValue();
                Result prevResult = healthStatusMap.put(name, result);

                if (prevResult != null && (result.isHealthy() != prevResult.isHealthy())) {
                    logger.info("## Name : {}'s health status is changed. {} -> {}"
                        , name
                        , prevResult.isHealthy() ? "Healthy" : "Unhealthy"
                        , result.isHealthy() ? "Healthy" : "Unhealthy"
                    );
                }
            }
        } finally {
            lock.writeLock().unlock();
        }
    }
}

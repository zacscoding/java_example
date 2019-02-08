package docker;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.async.ResultCallback;
import com.github.dockerjava.api.model.BlkioStatEntry;
import com.github.dockerjava.api.model.BlkioStatsConfig;
import com.github.dockerjava.api.model.Container;
import com.github.dockerjava.api.model.CpuStatsConfig;
import com.github.dockerjava.api.model.MemoryStatsConfig;
import com.github.dockerjava.api.model.StatisticNetworksConfig;
import com.github.dockerjava.api.model.Statistics;
import demo.DockerClientHelper;
import demo.NoStreamCallback;
import java.io.Closeable;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;


/**
 * @GitHub : https://github.com/zacscoding
 */
@Slf4j
public class DockerMetricsTest {

    List<String> containerIds;
    DockerClient docker;

    @Before
    public void setUp() {
        this.docker = DockerClientHelper.INSTANCE.getDockerClient();
        List<Container> containers = docker.listContainersCmd().withShowAll(true).exec();
        this.containerIds = containers.stream().map(container -> container.getId()).collect(Collectors.toList());
    }

    @After
    public void tearDown() throws Exception {
        this.docker.close();
    }

    @Test
    public void collectMetricsJob() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        for (int i = 0; i < 5; i++) {
            for (String containerId : containerIds) {
                NoStreamCallback<Statistics> statsCallback = new NoStreamCallback<>();
                docker.statsCmd(containerId).exec(statsCallback);
                Statistics stats = statsCallback.awaitNext();
                System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(stats));
            }

            TimeUnit.SECONDS.sleep(10L);
        }
    }

    @Test
    public void streamMetricsCallback() throws Exception {
        String containerId = "37a687553efa";
        ResultCallback<Statistics> callback = docker.statsCmd(containerId).exec(new ResultCallback<Statistics>() {
            private Closeable stream;
            private boolean running = false;

            @Override
            public void onStart(Closeable stream) {
                this.stream = stream;
                this.running = true;
            }

            @Override
            public void onNext(Statistics stats) {
                Long pid = stats.getPidsStats().getCurrent();
                if (pid == null) {
                    logger.warn("Found Null pid.....");
                    try {
                        close();
                    } catch (IOException e) {
                    }
                    return;
                }
                // logger.info("Stats :: {}", stats);
                StringBuilder metrics = new StringBuilder("\n");
                // cpu
                metrics.append(getCpuInfos(stats)).append(" | ");

                // memory
                metrics.append(getMemoryInfos(stats)).append(" | ");

                // block io
                metrics.append(getBlockIoInfos(stats)).append(" | ");

                // network
                metrics.append(getNetworkInfos(stats));

                logger.info(
                    Thread.currentThread().getName() + "-" + Thread.currentThread().getId() + metrics.toString()
                );
                /* output
                10:41:40.497 INFO [d.DockerMetricsTest]
                Cpu percentage :: 0.119919 % | Memory usage : 45670400.000000 / 4124770304.000000 > 1.1072% | Block IO :: read : 0.000000 / write : 8192.000000 | Network IO :: rx : 105085.000000 / tx : 53704.000000
                10:41:41.497 INFO [d.DockerMetricsTest]
                Cpu percentage :: 0.119484 % | Memory usage : 45670400.000000 / 4124770304.000000 > 1.1072% | Block IO :: read : 0.000000 / write : 8192.000000 | Network IO :: rx : 105085.000000 / tx : 53704.000000
                 */
            }

            @Override
            public void onError(Throwable throwable) {
                logger.error("Exception occur", throwable);
            }

            @Override
            public void onComplete() {
                logger.info("onComplete() is called");
            }

            @Override
            public void close() throws IOException {
                logger.info("close() is called");
                if (running && stream != null) {
                    running = false;
                    stream.close();
                }
            }
        });

        TimeUnit.MINUTES.sleep(2L);
        callback.close();
    }

    /**
     * https://github.com/moby/moby/blob/moby/cli/command/container/stats_helpers.go
     */
    private String getCpuInfos(Statistics stats) {
        try {
            CpuStatsConfig prevCpuStats = stats.getPreCpuStats();
            CpuStatsConfig cpuStats = stats.getCpuStats();

            double previousCpu = prevCpuStats.getCpuUsage().getTotalUsage();
            double previousSystem = prevCpuStats.getSystemCpuUsage();

            double cpuPercent = 0.0D;
            double cpuDelta = (double) cpuStats.getCpuUsage().getTotalUsage() - previousCpu;
            double systemDelta = (double) cpuStats.getSystemCpuUsage() - previousSystem;
            double onlineCpus = cpuStats.getOnlineCpus();

            if (onlineCpus == 0.0D) {
                onlineCpus = cpuStats.getCpuUsage().getPercpuUsage().size();
            }

            if (systemDelta > 0.0D && cpuDelta > 0.0D) {
                cpuPercent = (cpuDelta / systemDelta) * onlineCpus * 100.0D;
            }

            return String.format("Cpu percentage :: %.6f %%", cpuPercent);
        } catch (NullPointerException e) {
            return "Cpu percentage :: 0%";
        }
    }

    private String getMemoryInfos(Statistics stats) {
        MemoryStatsConfig memoryStats = stats.getMemoryStats();
        double usage = memoryStats.getUsage();
        double limit = memoryStats.getLimit();
        double percentage = 0.0D;

        if (limit != 0.0D) {
            percentage = usage / limit * 100.0D;
        }

        return String.format("Memory usage : %f / %f > %.4f%%", usage, limit, percentage);
    }

    private String getBlockIoInfos(Statistics stats) {
        BlkioStatsConfig blockIoStats = stats.getBlkioStats();
        double blockRead = 0.0D;
        double blockWrite = 0.0D;
        for (BlkioStatEntry entry : blockIoStats.getIoServiceBytesRecursive()) {

            switch (entry.getOp().toLowerCase()) {
                case "read":
                    blockRead += entry.getValue();
                    break;
                case "write":
                    blockWrite += entry.getValue();
            }
        }

        return String.format("Block IO :: read : %f / write : %f", blockRead, blockWrite);
    }

    private String getNetworkInfos(Statistics stats) {
        double rx = 0.0D;
        double tx = 0.0D;
        Map<String, StatisticNetworksConfig> networks = stats.getNetworks();
        for (Entry<String, StatisticNetworksConfig> entry : networks.entrySet()) {
            StatisticNetworksConfig networksConfig = entry.getValue();
            rx += networksConfig.getRxBytes();
            tx += networksConfig.getTxBytes();
        }

        return String.format("Network IO :: rx : %f / tx : %f", rx, tx);
    }
}

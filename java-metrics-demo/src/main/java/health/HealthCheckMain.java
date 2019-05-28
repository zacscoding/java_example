package health;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;

/**
 * @GitHub : https://github.com/zacscoding
 */
@Slf4j
public class HealthCheckMain {

    public static void run() throws Exception {
        HealthChecker.INSTANCE.start();

        for (int i = 1; i < 4; i++) {
            String nodeName = "node0" + i;
            HealthChecker.INSTANCE.register(nodeName, StubHealthCheck.newInstance(nodeName));
        }

        Thread displayThread = new Thread(() -> {
            try {
                while (!Thread.currentThread().isInterrupted()) {
                    List<String> healthyNodes = HealthChecker.INSTANCE.getHealthyNodes();
                    List<String> unhealthyNodes = HealthChecker.INSTANCE.getUnhealthyNodes();

                    StringBuilder builder = new StringBuilder()
                        .append("\n======================================================\n")
                        .append("## Display health status\n")
                        .append("> Healthy\n")
                        .append(
                            healthyNodes.stream().collect(Collectors.joining(" "))
                        )
                        .append("\n")
                        .append("> Unhealthy\n")
                        .append(
                            unhealthyNodes.stream().collect(Collectors.joining(" "))
                        );
                    logger.info(builder.toString());
                    TimeUnit.SECONDS.sleep(2L);
                }
            } catch (Exception e) {
                logger.warn("Exception occur while display health status");
            }
        });
        displayThread.setDaemon(true);
        displayThread.start();

        TimeUnit.SECONDS.sleep(15);
    }

    /*
    Output
    00:58:10.850 [pool-1-thread-1] INFO  health.StubHealthCheck - [StubHealthCheck] Health check node01 -> Unhealthy
    00:58:10.865 [pool-1-thread-1] INFO  health.StubHealthCheck - [StubHealthCheck] Health check node02 -> Unhealthy
    00:58:10.865 [pool-1-thread-1] INFO  health.StubHealthCheck - [StubHealthCheck] Health check node03 -> Healthy
    00:58:11.908 [Thread-1] INFO  health.HealthCheckMain -
    ======================================================
    ## Display health status
    > Healthy
    node03
    > Unhealthy
    node01 node02
    00:58:13.849 [pool-1-thread-1] INFO  health.StubHealthCheck - [StubHealthCheck] Health check node01 -> Healthy
    00:58:13.849 [pool-1-thread-1] INFO  health.StubHealthCheck - [StubHealthCheck] Health check node02 -> Unhealthy
    00:58:13.850 [pool-1-thread-1] INFO  health.StubHealthCheck - [StubHealthCheck] Health check node03 -> Unhealthy
    00:58:13.850 [pool-1-thread-1] INFO  health.HealthChecker - ## Name : node01's health status is changed. Unhealthy -> Healthy
    00:58:13.850 [pool-1-thread-1] INFO  health.HealthChecker - ## Name : node03's health status is changed. Healthy -> Unhealthy
    00:58:13.909 [Thread-1] INFO  health.HealthCheckMain -
    ======================================================
    ## Display health status
    > Healthy
    node01
    > Unhealthy
    node02 node03
     */
}

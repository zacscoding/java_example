package docker;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.model.Container;
import com.github.dockerjava.api.model.Statistics;
import demo.DockerClientHelper;
import demo.NoStreamCallback;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;


/**
 * @GitHub : https://github.com/zacscoding
 */
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
}

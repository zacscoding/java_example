package docker;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.model.Container;
import demo.DockerClientHelper;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.Before;
import org.junit.Test;

/**
 * @GitHub : https://github.com/zacscoding
 */
public class DockerContainerFindTest {

    DockerClient docker;

    @Before
    public void setUp() {
        this.docker = DockerClientHelper.INSTANCE.getDockerClient();
    }

    @Test
    public void findContainerById() {
        List<String> containerNames = Arrays.asList("boot", "cli");
        List<Container> results = docker.listContainersCmd().withNameFilter(containerNames).withShowAll(true).exec();
        String containerNamesString = containerNames.stream().collect(Collectors.joining(" "));
        System.out.printf("Container names :: %s >> results size : %d", containerNamesString, results.size());
    }
}

package docker;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.model.Container;
import com.google.common.base.Strings;
import demo.DockerClientHelper;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @GitHub : https://github.com/zacscoding
 */
public class DockerMetricsTest {

    Map<String, String> idNameMap = new HashMap<>();
    DockerClient docker;

    @Before
    public void setUp() {
        this.docker = DockerClientHelper.INSTANCE.getDockerClient();
        this.idNameMap = new HashMap<>();

        List<Container> containers = docker.listContainersCmd().withShowAll(true).exec();
        for (Container container : containers) {
            idNameMap.put(container.getId(), Arrays.toString(container.getNames()));
        }
    }

    @After
    public void tearDown() throws Exception {
        this.docker.close();
    }

    @Test
    public void temp() throws Exception {
    }

}

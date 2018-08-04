package docker.temp;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.model.Container;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientBuilder;
import java.util.List;
import java.util.Properties;
import org.junit.Before;
import org.junit.Test;

/**
 * http://www.baeldung.com/docker-java-api
 */
public class ConnectTest {

    // docker run -d -p 8080:8080 -e WHO="Zaccoding" example/docker-node-hello:latest
    private String userName;
    private String password;
    private String host;

    @Before
    public void setUp() throws Exception {
        Properties properties = new Properties();
        properties.load(ConnectTest.class.getClassLoader().getResourceAsStream("ignore-docker.properties"));

        userName = properties.getProperty("username");
        password = properties.getProperty("password");
        host = properties.getProperty("host");
    }

    @Test
    public void connect() throws Exception {
        DefaultDockerClientConfig config = DefaultDockerClientConfig.createDefaultConfigBuilder()
                                                                    .withRegistryUsername(userName)
                                                                    .withRegistryPassword(password)
                                                                    .withDockerHost(host)
                                                                    .build();

        DockerClient dockerClient = DockerClientBuilder.getInstance(config).build();
        List<Container> containers = dockerClient.listContainersCmd().withShowSize(true).withShowAll(true).withStatusFilter("exited").exec();

        for (Container container : containers) {
            System.out.println(container);
        }
    }
}

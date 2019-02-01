package demo;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientBuilder;
import lombok.extern.slf4j.Slf4j;

/**
 * @GitHub : https://github.com/zacscoding
 */
@Slf4j
public class DockerClientHelper {

    public static DockerClientHelper INSTANCE = new DockerClientHelper();

    private String username;
    private String password;
    private String host;

    private DockerClientHelper() {
        // TEMP FOR DEV > WILL CHANGED TO CONFIGS
        this.username = "app";
        this.password = "apppw";
        this.host = "tcp://192.168.5.78:2376";
    }

    /**
     * Getting docker client with tls false
     */
    public DockerClient getDockerClient() {
        try {
            DefaultDockerClientConfig config = DefaultDockerClientConfig.createDefaultConfigBuilder()
                .withRegistryUsername(username)
                .withRegistryPassword(password)
                .withDockerTlsVerify(false)
                .withDockerHost(host)
                .build();

            return DockerClientBuilder.getInstance(config).build();
        } catch (Exception e) {
            logger.warn("Exception occur while getting docker client", e);
            throw new RuntimeException(e);
        }
    }
}

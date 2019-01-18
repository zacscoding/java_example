package config;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import util.SimpleLogger;

/**
 * @author zacconding
 * @Date 2019-01-17
 * @GitHub : https://github.com/zacscoding
 */
public class Configurer extends Thread {

    // for configurer thread
    private static final String CONFIG_LOCATION = "agent_config_location";
    private static final Object LOCK = new Object();
    private static Configurer INSTANCE = Configurer.getInstance();
    private static Thread DAEMON;
    private static boolean RUNNING = true;

    private ObjectMapper objectMapper;
    private long lastModified;
    private File configFile;

    // agent properties
    private String serviceName;
    private List<String> serverUrls;

    public static boolean error() {
        return RUNNING == false;
    }

    public static Configurer getInstance() {
        synchronized (LOCK) {
            if (INSTANCE == null) {
                INSTANCE = new Configurer();
                DAEMON = INSTANCE;
                DAEMON.setDaemon(true);
                DAEMON.setName("Config-manager");
                DAEMON.start();
            }

            return INSTANCE;
        }
    }

    public String getServiceName() {
        return serviceName;
    }

    public List<String> getServerUrls() {
        return serverUrls;
    }

    private Configurer() {
        this.objectMapper = new ObjectMapper();
        this.lastModified = 0L;

        try {
            this.configFile = getConfigFile();
            loadConfiguration();
        } catch (Exception e) {
            RUNNING = false;
        }
    }

    @Override
    public void run() {
        try {
            while (RUNNING) {
                if (this.lastModified != configFile.lastModified()) {
                    loadConfiguration();
                }

                Thread.sleep(1000L);
            }
        } catch (Exception e) {
            RUNNING = false;
            // terminate this thread
        }
    }

    /**
     * Load configuration from config file & Settings last modified time
     */
    private boolean loadConfiguration() throws Exception {
        synchronized (LOCK) {
            try {
                JsonNode rootNode = objectMapper.readTree(configFile);
                serviceName = rootNode.get("service_name").asText();

                if (serverUrls == null) {
                    serverUrls = new ArrayList<>();
                } else {
                    serverUrls.clear();
                }

                for (JsonNode urlNode : rootNode.get("server_urls")) {
                    serverUrls.add(urlNode.asText());
                }

                lastModified = configFile.lastModified();

                SimpleLogger.println("Load properties. {}", lastModified);

                return true;
            } catch (Exception e) {
                e.printStackTrace();
                throw e;
            }
        }
    }

    private File getConfigFile() throws FileNotFoundException {
        String configLocation = System.getProperty(CONFIG_LOCATION);

        File configFile = new File(configLocation);
        if (!configFile.exists()) {
            throw new FileNotFoundException("file : " + configLocation);
        }

        return configFile;
    }
}

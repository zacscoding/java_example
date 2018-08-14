package tutorials.bindings.providesannotation;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Provides;
import util.SimpleLogger;

/**
 * @author zacconding
 * @Date 2018-08-14
 * @GitHub : https://github.com/zacscoding
 */
public class ProvideAnnotationMain {

    public static void main(String[] args) {
        SimpleLogger.println("## >> Check provide annotation binding..");

        Injector injector = Guice.createInjector(new DatabaseConnectorModule());
        DatabaseConnector connector = injector.getInstance(DatabaseConnector.class);
        connector.makeConnection();

        SimpleLogger.println("## >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
    }
}

class DatabaseConnector {

    private Connector connector;

    @Inject
    public DatabaseConnector(Connector connector) {
        this.connector = connector;
        SimpleLogger.println("## Check Connector inst : {} in DatabaseConnector", connector.getClass().getName());
    }

    public void makeConnection() {
        connector.connect();
    }
}

class DatabaseConnectorModule extends AbstractModule {

    @Override
    protected void configure() {
    }

    @Provides
    public Connector provideConnector() {
        String dbUrl = "jdbc:mysql://localhost:5326/emp";
        String user = "zaccoding";
        Integer timeout = Integer.valueOf(100);

        Connector connector = new JdbcConnector(dbUrl, user, timeout);
        return connector;
    }
}

interface Connector {

    void connect();
}

class JdbcConnector implements Connector {

    private String dbUrl;
    private String user;
    private Integer timeout;

    @Inject
    public JdbcConnector(String dbUrl, String user, Integer timeout) {
        this.dbUrl = dbUrl;
        this.user = user;
        this.timeout = timeout;
    }

    @Override
    public void connect() {
        SimpleLogger.build()
                    .appendln("## >> Try to connect in JdbcConnector")
                    .appendln("> dbUrl : {}", dbUrl)
                    .appendln("> user : {}", user)
                    .appendln("> timeout : {}", timeout)
                    .flush();
    }
}
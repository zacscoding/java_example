package org.jerseydemo.app;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

/**
 * @author zacconding
 * @Date 2018-01-31
 * @GitHub : https://github.com/zacscoding
 */
public class App {

    private static final int DEFAULT_PORT = 8090;
    private static final String CONTEXT_PATH = "/";

    public static void main(String[] args) throws Exception {
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath(CONTEXT_PATH);

        Server jettyServer = new Server(DEFAULT_PORT);
        jettyServer.setHandler(context);

        ServletHolder jerseyServlet = context.addServlet(org.glassfish.jersey.servlet.ServletContainer.class, "/*");
        jerseyServlet.setInitOrder(1);
        jerseyServlet.setInitParameter("javax.ws.rs.Application", ResourceLoader.class.getCanonicalName());
        System.out.println(ResourceLoader.class.getCanonicalName());

        try {
            jettyServer.start();
            jettyServer.join();
        } finally {
            jettyServer.destroy();
        }
    }
}

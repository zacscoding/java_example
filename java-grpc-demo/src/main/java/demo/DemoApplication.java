package demo;

import demo.routeguide.PersonRouteClient;
import demo.routeguide.PersonRouteServer;
import demo.sample1.client.Sample1GrpcClient;
import demo.sample1.server.Sample1GrpcServer;
import demo.util.SimpleLogger;
import java.util.concurrent.CountDownLatch;

/**
 * @GitHub : https://github.com/zacscoding
 */
public class DemoApplication {

    public static void main(String[] args) throws Exception {
        // SampleRunner runner = SampleRunner.SAMPLE1;
        SampleRunner runner = SampleRunner.ROUTE_GUIDE;

        SimpleLogger.println("Running samples {} - {}", runner, runner.getDescription());
        switch (runner) {
            case SAMPLE1:
                runSample1();
                break;
            case ROUTE_GUIDE:
                runRouteGuide();
                break;
            default:
                SimpleLogger.println("Invalid sample runner : " + runner);
        }
    }

    private static void runSample1() throws Exception {
        final String address = "127.0.0.1";
        final Integer port = 8888;

        Sample1GrpcServer server = new Sample1GrpcServer(port);
        server.start();

        Sample1GrpcClient client = new Sample1GrpcClient(address, port);
        client.request();

        Sample1GrpcClient client2 = new Sample1GrpcClient(address, port);
        client2.request();

        server.interrupt();
    }

    private static void runRouteGuide() throws Exception {
        SimpleLogger.println("## Start to route server");
        final CountDownLatch terminateLatch = new CountDownLatch(1);
        Thread serverThread = new Thread(() -> {
            try {
                PersonRouteServer server = new PersonRouteServer(PersonRouteServer.DEFAULT_PORT);
                server.start();
                server.blockUntilShutdown();
                terminateLatch.countDown();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        serverThread.setDaemon(true);
        serverThread.start();

        Thread clientThread = new Thread(() -> {
            try {
                String address = "localhost";
                int port = PersonRouteServer.DEFAULT_PORT;
                PersonRouteClient client = new PersonRouteClient(address, port);
                client.callRpcs();
                terminateLatch.countDown();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        clientThread.setDaemon(true);
        clientThread.start();

        terminateLatch.await();
    }
}

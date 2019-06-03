package demo;

import demo.routeguide.PersonRouteClient;
import demo.routeguide.PersonRouteServer;
import demo.sample1.client.Sample1GrpcClient;
import demo.sample1.server.Sample1GrpcServer;
import demo.tls.PersonTlsClient;
import demo.tls.PersonTlsServer;
import demo.util.SimpleLogger;
import java.util.concurrent.CountDownLatch;

/**
 * @GitHub : https://github.com/zacscoding
 */
public class DemoApplication {

    public static void main(String[] args) throws Exception {
        // SampleRunner runner = SampleRunner.SAMPLE1;
        // SampleRunner runner = SampleRunner.ROUTE_GUIDE;
        SampleRunner runner = SampleRunner.TLS;

        SimpleLogger.println("Running samples {} - {}", runner, runner.getDescription());
        switch (runner) {
            case SAMPLE1:
                runSample1();
                break;
            case ROUTE_GUIDE:
                runRouteGuide();
                break;
            case TLS:
                runTLS();
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

    private static void runTLS() throws Exception {
        // #### for server
        final String host = "localhost";
        final Integer port = 50440;
        final String certChainFilePath = "tls/server.crt";
        final String privateKeyFilePath = "tls/server.pem";
        // final String trustCertCollectionFilePath = null;
        final String trustCertCollectionFilePath = "tls/ca.crt";

        PersonTlsServer server = new PersonTlsServer(
            host,
            port,
            certChainFilePath,
            privateKeyFilePath,
            trustCertCollectionFilePath
        );

        server.start();
        // -- #### for server

        // #### for client
        String searchName = PersonTlsServer.PERSON_NAMES[0];

        // case 1) without cert
        SimpleLogger.println("## Try to request without cert");
        PersonTlsClient client = new PersonTlsClient(
            host, port, PersonTlsClient.buildSslContext(null, null, null)
        );
        client.getPersonsByName(searchName);
        client.shutdown();

        // case 2) request with TLS (no mutual auth)
        SimpleLogger.info("## Try to request with no mutual auth (only ca.crt)");
        client = new PersonTlsClient(
            host, port, PersonTlsClient.buildSslContext("tls/ca.crt", null, null)
        );
        client.getPersonsByName(searchName);
        client.shutdown();

        // case 3) request with TLS (mutual auth)
        SimpleLogger.info("## Try to request with mutual auth");
        client = new PersonTlsClient(
            host, port,
            PersonTlsClient.buildSslContext(
                "tls/ca.crt",
                "tls/client.crt",
                "tls/client.pem"
            )
        );
        client.getPersonsByName(searchName);
        client.shutdown();

        // -- #### for client
    }
}

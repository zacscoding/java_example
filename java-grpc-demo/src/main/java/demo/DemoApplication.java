package demo;

import demo.sample1.client.Sample1GrpcClient;
import demo.sample1.server.Sample1GrpcServer;
import demo.util.SimpleLogger;

/**
 * @GitHub : https://github.com/zacscoding
 */
public class DemoApplication {

    public static void main(String[] args) throws Exception {
        SampleRunner runner = SampleRunner.SAMPLE1;

        SimpleLogger.println("Running samples {} - {}", runner, runner.getDescription());
        switch (runner) {
            case SAMPLE1:
                runSample1();
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
}

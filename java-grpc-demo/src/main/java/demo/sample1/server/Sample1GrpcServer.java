package demo.sample1.server;

import demo.util.SimpleLogger;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import java.util.Objects;

/**
 * https://github.com/eugenp/tutorials/tree/master/grpc/
 */
public class Sample1GrpcServer extends Thread {

    private Server server;
    private int port;

    public Sample1GrpcServer(Integer port) {
        super.setDaemon(true);
        this.port = Objects.requireNonNull(port, "port must be not null");
    }

    @Override
    public void run() {
        if (isRunning()) {
            SimpleLogger.info("Already sample1 grpc server is running", null);
            return;
        }

        try {
            SimpleLogger.info("[Server] Try to start sample1 grpc server with {}", port);
            server = ServerBuilder.forPort(port)
                .addService(new HelloServiceImpl())
                .build();

            server = server.start();
            server.awaitTermination();
        } catch (Exception e) {
            if (isRunning()) {
                server.shutdown();
            }

            if (e instanceof InterruptedException) {
                return;
            }
            SimpleLogger.error("[Server] Failed to start sample1 grcp server", e);
        }
    }

    public boolean isRunning() {
        return server != null
            && !server.isShutdown()
            && !server.isTerminated();
    }
}

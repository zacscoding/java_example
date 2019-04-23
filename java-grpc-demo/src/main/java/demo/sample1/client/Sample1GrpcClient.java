package demo.sample1.client;

import demo.sample1.protos.HelloRequest;
import demo.sample1.protos.HelloResponse;
import demo.sample1.protos.HelloServiceGrpc;
import demo.util.SimpleLogger;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import java.util.Objects;

/**
 * https://github.com/eugenp/tutorials/tree/master/grpc/
 */
public class Sample1GrpcClient {

    private String address;
    private int port;

    public Sample1GrpcClient(String address, Integer port) {
        this.address = Objects.requireNonNull(address, "address must be not null");
        this.port = Objects.requireNonNull(port, "port must be not null");
    }

    public void request() {
        ManagedChannel channel = ManagedChannelBuilder.forAddress(address, port)
            .usePlaintext()
            .build();

        HelloServiceGrpc.HelloServiceBlockingStub stub = HelloServiceGrpc.newBlockingStub(channel);

        HelloResponse helloResponse = stub.hello(
            HelloRequest.newBuilder()
                .setFirstName("zaccoding")
                .setLastName("gRPC")
                .build()
        );

        SimpleLogger.info("Received gRPC response : {}", helloResponse);

        channel.shutdown();
    }
}

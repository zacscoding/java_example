package demo.sample1.server;

import demo.sample1.protos.HelloRequest;
import demo.sample1.protos.HelloResponse;
import demo.sample1.protos.HelloServiceGrpc.HelloServiceImplBase;
import demo.util.SimpleLogger;
import io.grpc.stub.StreamObserver;

/**
 * https://github.com/eugenp/tutorials/tree/master/grpc/
 */
public class HelloServiceImpl extends HelloServiceImplBase {

    @Override
    public void hello(HelloRequest request, StreamObserver<HelloResponse> responseObserver) {
        SimpleLogger.println("[Sample1HelloService] Request received from client : \n{}", request);

        String greeting = new StringBuilder().append("Hello,")
            .append(request.getFirstName())
            .append(" ")
            .append(request.getLastName())
            .toString();

        HelloResponse response = HelloResponse.newBuilder()
            .setGreeting(greeting)
            .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}

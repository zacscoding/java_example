package demo.routeguide;

import demo.routeguide.proto.PersonQuery;
import demo.routeguide.proto.PersonRequest;
import demo.routeguide.proto.PersonResponse;
import demo.routeguide.proto.PersonRouteGrpc;
import demo.routeguide.proto.PersonRouteGrpc.PersonRouteBlockingStub;
import demo.routeguide.proto.PersonRouteGrpc.PersonRouteStub;
import demo.routeguide.proto.PersonSaveSummary;
import demo.util.SimpleLogger;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import java.util.Iterator;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * https://github.com/grpc/grpc-java/tree/master/examples/src/main/java/io/grpc/examples/routeguide
 */
public class PersonRouteClient {

    private final ManagedChannel channel;
    private final PersonRouteBlockingStub blockingStub;
    private final PersonRouteStub asyncStub;

    public PersonRouteClient(String address, int port) {
        Objects.requireNonNull(address, "address must be not null");
        Objects.requireNonNull(port, "port must be not null");

        SimpleLogger.println("Create person route client. address : {} / port : {}", address, port);

        this.channel = ManagedChannelBuilder.forAddress(address, port).usePlaintext().build();
        this.blockingStub = PersonRouteGrpc.newBlockingStub(channel);
        this.asyncStub = PersonRouteGrpc.newStub(channel);
    }

    public void callGetPerson(Long id) {
        SimpleLogger.println("====================================================");
        SimpleLogger.println("[Client] try to call getPerson()");
        PersonQuery query = PersonQuery.newBuilder()
            .setId(id)
            .build();

        PersonResponse response = blockingStub.getPerson(query);
        SimpleLogger.println("receive person response : {}", response);
        SimpleLogger.println("====================================================");
    }

    public void callListPerson(String name) {
        SimpleLogger.println("====================================================");

        SimpleLogger.println("[Client] try to call ListPerson()");
        PersonQuery query = PersonQuery.newBuilder()
            .setName(name)
            .build();

        Iterator<PersonResponse> itr = blockingStub.listPerson(query);
        int count = 0;
        while (itr.hasNext()) {
            count++;
            SimpleLogger.println("receive person response : {}", itr.next());
        }

        SimpleLogger.println("==> total count : {}", count);
        SimpleLogger.println("====================================================");
    }

    public void callSavePerson() throws InterruptedException {
        SimpleLogger.println("====================================================");
        SimpleLogger.println("[Client] try to call savePerson()");
        final CountDownLatch finishLatch = new CountDownLatch(1);
        StreamObserver<PersonSaveSummary> responseObserver = new StreamObserver<PersonSaveSummary>() {
            @Override
            public void onNext(PersonSaveSummary personSaveSummary) {
                SimpleLogger.println("[Receive person save summary] : {}", personSaveSummary);
            }

            @Override
            public void onError(Throwable throwable) {
                SimpleLogger.error("onError...", throwable);
            }

            @Override
            public void onCompleted() {
                SimpleLogger.println("Finished SavePerson");
                finishLatch.countDown();
            }
        };

        StreamObserver<PersonRequest> requestObserver = asyncStub.savePerson(responseObserver);
        try {
            for (int i = 0; i < 10; i++) {
                String name = "hivava";
                if (i % 3 == 0) {
                    name = "awsome";
                }

                requestObserver.onNext(
                    PersonRequest.newBuilder()
                        .setName(name)
                        .setAge(i)
                        .build()
                );
            }
        } catch (Exception e) {
            requestObserver.onError(e);
        }

        requestObserver.onCompleted();

        if (!finishLatch.await(1, TimeUnit.MINUTES)) {
            SimpleLogger.error("savePerson cannot finish within 1 minutes", null);
        }

        SimpleLogger.println("====================================================");
    }

    public void callGetPersonChat() throws InterruptedException {
        SimpleLogger.println("====================================================");
        SimpleLogger.println("[Client] try to call getPersonChat()");
        final CountDownLatch finishLatch = new CountDownLatch(1);
        StreamObserver<PersonQuery> requestObserver = asyncStub.getPersonChat(
            new StreamObserver<PersonResponse>() {
                @Override
                public void onNext(PersonResponse personResponse) {
                    SimpleLogger.println("[onNext] {}", personResponse);
                }

                @Override
                public void onError(Throwable throwable) {
                    SimpleLogger.error("Exception occur", throwable);
                    finishLatch.countDown();
                }

                @Override
                public void onCompleted() {
                    SimpleLogger.println("[onCompleted]");
                    finishLatch.countDown();
                }
            }
        );

        try {
            for (int i = 5; i < 15; i++) {
                requestObserver.onNext(
                    PersonQuery.newBuilder()
                        .setId(i)
                        .build()
                );
                TimeUnit.SECONDS.sleep(new Random().nextInt(3));
            }
        } catch (Exception e) {
            requestObserver.onError(e);
        }

        requestObserver.onCompleted();

        if (!finishLatch.await(1, TimeUnit.MINUTES)) {
            SimpleLogger.error("savePerson cannot finish within 1 minutes", null);
        }

        SimpleLogger.println("====================================================");
    }

    public void shutdown() throws InterruptedException {
        channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
    }

    public void callRpcs() throws Exception {
        String address = "localhost";
        int port = PersonRouteServer.DEFAULT_PORT;

        PersonRouteClient client = new PersonRouteClient(address, port);

        // simple rpc
        // Obtains a person at a given id
        callGetPerson(0L);

        // server-to-client streaming RPC
        // Obtains persons within the given name
        callListPerson("hiva");

        // client-to-server streaming RPC
        // Save persons and return a PersonSaveSummary
        callSavePerson();

        // bidirectional streaming RPC
        callGetPersonChat();

        shutdown();
    }

    public static void main(String[] args) throws Exception {

    }
}

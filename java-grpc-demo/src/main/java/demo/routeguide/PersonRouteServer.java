package demo.routeguide;

import demo.person.MapPersonRepository;
import demo.person.Person;
import demo.person.PersonRepository;
import demo.routeguide.proto.PersonQuery;
import demo.routeguide.proto.PersonRequest;
import demo.routeguide.proto.PersonResponse;
import demo.routeguide.proto.PersonRouteGrpc;
import demo.routeguide.proto.PersonSaveSummary;
import demo.util.SimpleLogger;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Random;

/**
 * https://github.com/grpc/grpc-java/tree/master/examples/src/main/java/io/grpc/examples/routeguide
 */
public class PersonRouteServer {

    public static final int DEFAULT_PORT = 12883;

    private final int port;
    private final Server server;
    private PersonRepository personRepository;

    public PersonRouteServer(int port) {
        this.personRepository = new MapPersonRepository();
        this.port = port;
        this.server = ServerBuilder.forPort(port)
            .addService(new RouteGuideService(personRepository))
            .build();

        // dummy data
        for (int i = 0; i < 10; i++) {
            String name = i % 2 == 0 ? "hiva" : "zaccoding";
            personRepository.save(
                Person.builder()
                    .name(name)
                    .age(new Random().nextInt(100) + 5)
                    .build()
            );
        }
    }

    public void start() throws IOException {
        server.start();
        SimpleLogger.println("Server started, listening on {}", port);
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            PersonRouteServer.this.stop();
        }));
    }

    public void stop() {
        if (server != null) {
            server.shutdown();
        }
    }

    public void blockUntilShutdown() throws InterruptedException {
        if (server != null) {
            server.awaitTermination();
        }
    }

    private static class RouteGuideService extends PersonRouteGrpc.PersonRouteImplBase {

        private PersonRepository personRepository;

        public RouteGuideService(PersonRepository personRepository) {
            this.personRepository = personRepository;
        }

        @Override
        public void getPerson(PersonQuery request, StreamObserver<PersonResponse> responseObserver) {
            SimpleLogger.println("====================================================");
            SimpleLogger.println("[Server] called getPerson(), request : {}", request);

            Optional<Person> optionalPerson = personRepository.findById(request.getId());

            if (!optionalPerson.isPresent()) {
                responseObserver.onNext(null);
            } else {
                responseObserver.onNext(convertPersonToPersonResponse(optionalPerson.get()));
            }

            responseObserver.onCompleted();
            SimpleLogger.println("====================================================");
        }

        @Override
        public void listPerson(PersonQuery request, StreamObserver<PersonResponse> responseObserver) {
            SimpleLogger.println("====================================================");
            SimpleLogger.println("[Server] called listPerson(), request : {}", request);

            List<Person> persons = personRepository.findAllByName(request.getName());
            for (Person person : persons) {
                responseObserver.onNext(convertPersonToPersonResponse(person));
            }

            responseObserver.onCompleted();
            SimpleLogger.println("====================================================");
        }

        @Override
        public StreamObserver<PersonRequest> savePerson(StreamObserver<PersonSaveSummary> responseObserver) {
            return new StreamObserver<PersonRequest>() {
                long start = System.currentTimeMillis();
                int trial = 0;
                int success = 0;
                int fail = 0;

                @Override
                public void onNext(PersonRequest personRequest) {
                    SimpleLogger.println("[Server] onNext({})", personRequest);
                    trial++;

                    if (personRequest.getName().startsWith("a")) {
                        fail++;
                    }

                    personRepository.save(convertPersonRequestToPerson(personRequest));
                    success++;
                }

                @Override
                public void onError(Throwable throwable) {
                    SimpleLogger.error("onError is called..", throwable);
                }

                @Override
                public void onCompleted() {
                    long elapsed = System.currentTimeMillis() - start;

                    responseObserver.onNext(
                        PersonSaveSummary.newBuilder()
                            .setTrial(trial)
                            .setSuccess(success)
                            .setFail(fail)
                            .setElapsed(elapsed)
                            .build()
                    );

                    responseObserver.onCompleted();
                }
            };
        }

        @Override
        public StreamObserver<PersonQuery> getPersonChat(StreamObserver<PersonResponse> responseObserver) {
            return new StreamObserver<PersonQuery>() {
                @Override
                public void onNext(PersonQuery personQuery) {
                    SimpleLogger.println("[onNext] query : {}", personQuery);
                    Optional<Person> optionalPerson = personRepository.findById(personQuery.getId());
                    optionalPerson.ifPresent(p -> responseObserver.onNext(convertPersonToPersonResponse(p)));
                }

                @Override
                public void onError(Throwable throwable) {
                    SimpleLogger.error("Exception occur", throwable);
                }

                @Override
                public void onCompleted() {
                    responseObserver.onCompleted();
                }
            };
        }

        private PersonResponse convertPersonToPersonResponse(Person person) {
            if (person == null) {
                return null;
            }

            return PersonResponse.newBuilder()
                .setName(person.getName())
                .setId(person.getId())
                .setAge(person.getAge())
                .build();
        }

        private Person convertPersonRequestToPerson(PersonRequest personRequest) {
            return Person.builder()
                .name(personRequest.getName())
                .age(personRequest.getAge())
                .build();
        }
    }

    public static void main(String[] args) throws Exception {
        SimpleLogger.println("## Start to route server");
        PersonRouteServer server = new PersonRouteServer(PersonRouteServer.DEFAULT_PORT);
        server.start();
        server.blockUntilShutdown();
    }
}

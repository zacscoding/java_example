package demo.tls;

import demo.person.MapPersonRepository;
import demo.person.Person;
import demo.person.PersonRepository;
import demo.tls.proto.PersonReply;
import demo.tls.proto.PersonRequest;
import demo.tls.proto.PersonTLSGrpc;
import demo.tls.proto.PersonTls;
import demo.util.SimpleLogger;
import io.grpc.Server;
import io.grpc.netty.GrpcSslContexts;
import io.grpc.netty.NettyServerBuilder;
import io.grpc.stub.StreamObserver;
import io.netty.handler.ssl.ClientAuth;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.SslProvider;
import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.List;
import java.util.Random;

/**
 * Person gRpc server with TLS
 *
 * https://github.com/grpc/grpc-java/tree/master/examples/example-tls
 */
public class PersonTlsServer {

    public static final String[] PERSON_NAMES = {"hiva", "zac", "brad", "air"};

    private Server server;
    private final String host;
    private final int port;
    private final String certChainFilePath;
    private final String privateKeyFilePath;
    private final String trustCertCollectionFilePath;

    private PersonRepository personRepository;

    public PersonTlsServer(String host, int port, String certChainFilePath, String privateKeyFilePath,
        String trustCertCollectionFilePath) {
        this.host = host;
        this.port = port;
        this.certChainFilePath = certChainFilePath;
        this.privateKeyFilePath = privateKeyFilePath;
        this.trustCertCollectionFilePath = trustCertCollectionFilePath;

        this.personRepository = new MapPersonRepository();
        addInitialPersonDatas();
    }

    public void start() throws IOException {
        server = NettyServerBuilder.forAddress(new InetSocketAddress(host, port))
            .addService(new PersonTlsService(personRepository))
            .sslContext(getSslContextBuilder().build())
            .build()
            .start();

        SimpleLogger.info("Server started, listening on {}", port);
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            PersonTlsServer.this.stop();
        }));
    }

    public void stop() {
        if (server != null) {
            server.shutdown();
        }
    }

    private SslContextBuilder getSslContextBuilder() {
        SslContextBuilder sslClientContextBuilder = SslContextBuilder.forServer(
            PersonTlsServer.class.getClassLoader().getResourceAsStream(certChainFilePath),
            PersonTlsServer.class.getClassLoader().getResourceAsStream(privateKeyFilePath)
        );

        if (trustCertCollectionFilePath != null) {
            sslClientContextBuilder.trustManager(
                PersonTlsServer.class.getClassLoader().getResourceAsStream(trustCertCollectionFilePath)
            );
            sslClientContextBuilder.clientAuth(ClientAuth.REQUIRE);
        }

        return GrpcSslContexts.configure(
            sslClientContextBuilder, SslProvider.OPENSSL
        );
    }

    /**
     * Initialize person data
     */
    private void addInitialPersonDatas() {
        String[] names = PERSON_NAMES;

        for (int i = 0; i < 20; i++) {
            int randNameIdx = new Random().nextInt(names.length);
            personRepository.save(
                Person.builder()
                    .name(names[randNameIdx])
                    .age(new Random().nextInt(80))
                    .build()
            );
        }
    }

    /**
     * PersonTLSGrpc impl
     */
    private static class PersonTlsService extends PersonTLSGrpc.PersonTLSImplBase {

        private final PersonRepository personRepository;

        public PersonTlsService(PersonRepository personRepository) {
            this.personRepository = personRepository;
        }

        @Override
        public void getPersonsByName(PersonRequest request, StreamObserver<PersonReply> responseObserver) {
            List<Person> persons = personRepository.findAllByName(request.getName());

            for (Person person : persons) {
                responseObserver.onNext(
                    PersonReply.newBuilder()
                        .setId(person.getId())
                        .setName(person.getName())
                        .setAge(person.getAge())
                        .build()
                );
            }

            responseObserver.onCompleted();
        }
    }
}

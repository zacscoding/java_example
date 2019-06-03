package demo.tls;

import demo.tls.proto.PersonReply;
import demo.tls.proto.PersonRequest;
import demo.tls.proto.PersonTLSGrpc;
import demo.util.SimpleLogger;
import io.grpc.ManagedChannel;
import io.grpc.StatusRuntimeException;
import io.grpc.netty.GrpcSslContexts;
import io.grpc.netty.NegotiationType;
import io.grpc.netty.NettyChannelBuilder;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import java.io.IOException;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;
import javax.net.ssl.SSLException;

/**
 * https://github.com/grpc/grpc-java/tree/master/examples/example-tls
 */
public class PersonTlsClient {

    private final ManagedChannel channel;
    private final PersonTLSGrpc.PersonTLSBlockingStub blockingStub;

    public static SslContext buildSslContext(String trustCertCollectionFilePath,
        String clientCertChainFilePath,
        String clientPrivateKeyFilePath) throws IOException {

        SslContextBuilder builder = GrpcSslContexts.forClient();
        if (trustCertCollectionFilePath != null) {
            builder.trustManager(
                PersonTlsClient.class.getClassLoader().getResourceAsStream(trustCertCollectionFilePath)
            );
        }

        if (clientCertChainFilePath != null && clientPrivateKeyFilePath != null) {
            builder.keyManager(
                PersonTlsClient.class.getClassLoader().getResourceAsStream(clientCertChainFilePath),
                PersonTlsClient.class.getClassLoader().getResourceAsStream(clientPrivateKeyFilePath)
            );
        }

        return builder.build();
    }


    public PersonTlsClient(String host, int port, SslContext sslContext) throws SSLException {
        this.channel = NettyChannelBuilder.forAddress(host, port)
            .negotiationType(NegotiationType.TLS)
            .sslContext(sslContext)
            .build();

        this.blockingStub = PersonTLSGrpc.newBlockingStub(channel);
    }

    public void shutdown() throws InterruptedException {
        channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
    }

    public void getPersonsByName(String name) {
        SimpleLogger.info("Try to request person by name : {}", name);

        PersonRequest request = PersonRequest.newBuilder().setName(name).build();
        try {
            Iterator<PersonReply> personReplyIterator = blockingStub.getPersonsByName(request);
            SimpleLogger.info("Received person reply");
            int idx = 1;
            while (personReplyIterator.hasNext()) {
                PersonReply personReply = personReplyIterator.next();
                SimpleLogger.info("[{}] id : {} | name : {} | age : {}",
                    idx++, personReply.getId(), personReply.getName(), personReply.getAge());
            }
        } catch (StatusRuntimeException e) {
            SimpleLogger.println("RPC failed : {} >> {}", e.getStatus(), e.getMessage());
            return;
        }
    }
}

package demo;

/**
 * @GitHub : https://github.com/zacscoding
 */
public enum SampleRunner {

    SAMPLE1("Basic grpc server & client"),
    ROUTE_GUIDE("Route example"),
    TLS("gRpc with TLS");

    String description;

    SampleRunner(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}

package metrics1;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * https://github.com/elastic/apm-agent-java/blob/master/apm-agent-core/src/main/java/co/elastic/apm/agent/metrics/MetricSet.java
 */

/**
 * A metric set is a collection of metrics which have the same tags.
 * <p>
 * A metric set corresponds to one document per {@link co.elastic.apm.agent.report.ReporterConfiguration#metricsInterval
 * metrics_interval} in Elasticsearch. An alternative would be to have one document per metric but having one document
 * for all metrics with the same tags saves disk space.
 * </p>
 * Example of some serialized metric sets:
 * <pre>
 * {"metricset":{"timestamp":1545047730692000,"samples":{"jvm.gc.alloc":{"value":24089200.0}}}}
 * {"metricset":{"timestamp":1545047730692000,"tags":{"name":"G1 Young Generation"},"samples":{"jvm.gc.time":{"value":0.0},"jvm.gc.count":{"value":0.0}}}}
 * {"metricset":{"timestamp":1545047730692000,"tags":{"name":"G1 Old Generation"},  "samples":{"jvm.gc.time":{"value":0.0},"jvm.gc.count":{"value":0.0}}}}
 * </pre>
 */
public class MetricSet1 {

    private final Map<String, String> tags;
    private final ConcurrentMap<String, DoubleSupplier> samples = new ConcurrentHashMap<>();

    public MetricSet1(Map<String, String> tags) {
        this.tags = tags;
    }

    public void add(String name, DoubleSupplier metric) {
        samples.putIfAbsent(name, metric);
    }

    DoubleSupplier get(String name) {
        return samples.get(name);
    }

    public Map<String, String> getTags() {
        return tags;
    }

    public Map<String, DoubleSupplier> getSamples() {
        return samples;
    }
}

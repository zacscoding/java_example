/*-
 * #%L
 * Elastic APM Java agent
 * %%
 * Copyright (C) 2018 - 2019 Elastic and contributors
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package metrics1;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.util.Collections;

/**
 * https://github.com/elastic/apm-agent-java/blob/master/apm-agent-core/src/main/java/co/elastic/apm/agent/metrics/builtin/JvmMemoryMetrics.java
 */
public class JvmMemoryMetrics1 {

    // changed code for testing
    public void start(MetricRegistry1 registry) {
        final MemoryMXBean platformMXBean = ManagementFactory.getPlatformMXBean(MemoryMXBean.class);
        registry.add("jvm.memory.heap.used", Collections.<String, String>emptyMap(), new DoubleSupplier() {
            @Override
            public double get() {
                return platformMXBean.getHeapMemoryUsage().getUsed();
            }
        });
        registry.add("jvm.memory.heap.committed", Collections.<String, String>emptyMap(), new DoubleSupplier() {
            @Override
            public double get() {
                return platformMXBean.getHeapMemoryUsage().getCommitted();
            }
        });
        registry.add("jvm.memory.heap.max", Collections.<String, String>emptyMap(), new DoubleSupplier() {
            @Override
            public double get() {
                return platformMXBean.getHeapMemoryUsage().getMax();
            }
        });
        registry.add("jvm.memory.non_heap.used", Collections.<String, String>emptyMap(), new DoubleSupplier() {
            @Override
            public double get() {
                return platformMXBean.getNonHeapMemoryUsage().getUsed();
            }
        });
        registry.add("jvm.memory.non_heap.committed", Collections.<String, String>emptyMap(), new DoubleSupplier() {
            @Override
            public double get() {
                return platformMXBean.getNonHeapMemoryUsage().getCommitted();
            }
        });
        registry.add("jvm.memory.non_heap.max", Collections.<String, String>emptyMap(), new DoubleSupplier() {
            @Override
            public double get() {
                return platformMXBean.getNonHeapMemoryUsage().getMax();
            }
        });
    }
}

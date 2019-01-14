package metrics1;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

/**
 * https://github.com/elastic/apm-agent-java/blob/master/apm-agent-core/src/main/java/co/elastic/apm/agent/metrics/builtin/SystemMetrics.java
 */

/**
 * Record metrics related to the CPU, gathered by the JVM.
 * <p>
 * Supported JVM implementations:
 * <ul>
 * <li>HotSpot</li>
 * <li>J9</li>
 * </ul>
 * <p>
 * This implementation is based on io.micrometer.core.instrument.binder.system.ProcessorMetrics, under Apache License
 * 2.0
 */
@Slf4j
public class SystemMetrics1 {

    /**
     * List of public, exported interface class names from supported JVM implementations.
     */
    private static final List<String> OPERATING_SYSTEM_BEAN_CLASS_NAMES = Arrays.asList(
        "com.sun.management.OperatingSystemMXBean", // HotSpot
        "com.ibm.lang.management.OperatingSystemMXBean" // J9
    );

    private final OperatingSystemMXBean operatingSystemBean;

    private final Class<?> operatingSystemBeanClass;

    private final MethodHandle systemCpuUsage;

    private final MethodHandle processCpuUsage;

    private final MethodHandle freeMemory;

    private final MethodHandle totalMemory;

    private final MethodHandle virtualProcessMemory;

    public SystemMetrics1() {
        this.operatingSystemBean = ManagementFactory.getOperatingSystemMXBean();
        this.operatingSystemBeanClass = getFirstClassFound(OPERATING_SYSTEM_BEAN_CLASS_NAMES);
        this.systemCpuUsage = detectMethod("getSystemCpuLoad", double.class);
        this.processCpuUsage = detectMethod("getProcessCpuLoad", double.class);
        this.freeMemory = detectMethod("getFreePhysicalMemorySize", long.class);
        this.totalMemory = detectMethod("getTotalPhysicalMemorySize", long.class);
        this.virtualProcessMemory = detectMethod("getCommittedVirtualMemorySize", long.class);
    }

    // change code for testing
    public void start(MetricRegistry1 metricRegistry) {
        metricRegistry.addUnlessNegative("system.cpu.total.norm.pct", Collections.<String, String>emptyMap(),
            new DoubleSupplier() {
                @Override
                public double get() {
                    return invoke(systemCpuUsage);
                }
            });

        metricRegistry.addUnlessNegative("system.process.cpu.total.norm.pct", Collections.<String, String>emptyMap(),
            new DoubleSupplier() {
                @Override
                public double get() {
                    return invoke(processCpuUsage);
                }
            });

        metricRegistry
            .addUnlessNan("system.memory.total", Collections.<String, String>emptyMap(), new DoubleSupplier() {
                @Override
                public double get() {
                    return invoke(totalMemory);
                }
            });

        metricRegistry
            .addUnlessNan("system.memory.actual.free", Collections.<String, String>emptyMap(), new DoubleSupplier() {
                @Override
                public double get() {
                    return invoke(freeMemory);
                }
            });

        metricRegistry.addUnlessNegative("system.process.memory.size", Collections.<String, String>emptyMap(),
            new DoubleSupplier() {
                @Override
                public double get() {
                    return invoke(virtualProcessMemory);
                }
            });
    }

    private double invoke(MethodHandle method) {
        try {
            return method != null ? (double) method.invoke(operatingSystemBean) : Double.NaN;
        } catch (Throwable e) {
            return Double.NaN;
        }
    }

    private Class<?> getFirstClassFound(List<String> classNames) {
        for (String className : classNames) {
            try {
                Class<?> ret = Class.forName(className);
                log.info("# Found class : {}", className);
                return ret;
            } catch (ClassNotFoundException ignore) {
                log.info("## Failed to find : {}", className);
            }
        }
        return null;
    }

    private MethodHandle detectMethod(String name, Class<?> returnType) {
        if (operatingSystemBeanClass == null) {
            return null;
        }
        try {
            // ensure the Bean we have is actually an instance of the interface
            operatingSystemBeanClass.cast(operatingSystemBean);
            return MethodHandles.lookup()
                .findVirtual(operatingSystemBeanClass, name, MethodType.methodType(returnType));
        } catch (ClassCastException | NoSuchMethodException | SecurityException | IllegalAccessException e) {
            return null;
        }
    }
}
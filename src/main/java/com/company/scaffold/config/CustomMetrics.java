package com.company.scaffold.config;

import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.binder.MeterBinder;
import org.springframework.stereotype.Component;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;

@Component
public class CustomMetrics implements MeterBinder {

    @Override
    public void bindTo(MeterRegistry registry) {
        // 内存使用情况
        MemoryMXBean memoryMXBean = ManagementFactory.getMemoryMXBean();
        Gauge.builder("jvm.memory.used", memoryMXBean, bean -> bean.getHeapMemoryUsage().getUsed())
                .description("Used heap memory")
                .baseUnit("bytes")
                .register(registry);

        Gauge.builder("jvm.memory.max", memoryMXBean, bean -> bean.getHeapMemoryUsage().getMax())
                .description("Max heap memory")
                .baseUnit("bytes")
                .register(registry);

        // 可以添加更多自定义指标
        // 例如：API调用次数、数据库查询时间等
    }
}

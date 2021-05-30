package com.test.ingestion.metrics;

import io.micrometer.core.instrument.Metrics;
import io.micrometer.prometheus.PrometheusConfig;
import io.micrometer.prometheus.PrometheusMeterRegistry;
import io.prometheus.client.CollectorRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

import java.util.HashMap;
import java.util.Map;
import io.prometheus.client.exporter.PushGateway;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.Scheduled;

import javax.annotation.PostConstruct;

@Slf4j
@EnableConfigurationProperties
@PropertySource("metrics-sample.properties")
public class PrometheusConfiguration {

    @Value("${prometheus.job.name}")
    private String prometheusJobName;

    @Value("${prometheus.grouping.key}")
    private String prometheusGroupingKey;

    @Value("${prometheus.pushgateway.url}")
    private String prometheusPushGatewayUrl;

    private Map<String, String> groupingKey = new HashMap<>();
    private PushGateway pushGateway;
    private CollectorRegistry collectorRegistry;

    @PostConstruct
    public void init() {
        pushGateway = new PushGateway(prometheusPushGatewayUrl);
        groupingKey.put(prometheusGroupingKey, prometheusJobName);
        PrometheusMeterRegistry prometheusMeterRegistry = new PrometheusMeterRegistry(PrometheusConfig.DEFAULT);
        collectorRegistry = prometheusMeterRegistry.getPrometheusRegistry();
        Metrics.globalRegistry.add(prometheusMeterRegistry);
    }

    @Scheduled(fixedRateString = "${prometheus.push.rate}")
    public void pushMetrics() {
        try {
            pushGateway.pushAdd(collectorRegistry, prometheusJobName, groupingKey);
        } catch (Throwable ex) {
            log.error("Unable to push metrics to Prometheus Push Gateway", ex);
        }
    }
}

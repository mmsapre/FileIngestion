package com.test.ingestion.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties
public class KafkaProperties {

    @Value("${Kafka.BootstrapServers}")
    private String kafkaBootstrapServer;
    @Value("${Kafka.ZookeeperHost}")
    private String kafkaZookeeper;
    @Value("${Kafka.topic}")
    private String kafkaTopic;
    @Value("${Kafka.dlq.topic}")
    private String dlqKafkaTopic;
    public String getKafkaBootstrapServer() {
        return kafkaBootstrapServer;
    }

    public void setKafkaBootstrapServer(String kafkaBootstrapServer) {
        this.kafkaBootstrapServer = kafkaBootstrapServer;
    }

    public String getKafkaTopic() {
        return kafkaTopic;
    }

    public void setKafkaTopic(String kafkaTopic) {
        this.kafkaTopic = kafkaTopic;
    }

    public String getDlqKafkaTopic() {
        return dlqKafkaTopic;
    }

    public void setDlqKafkaTopic(String dlqKafkaTopic) {
        this.dlqKafkaTopic = dlqKafkaTopic;
    }

    public String getKafkaZookeeper() {
        return kafkaZookeeper;
    }

    public void setKafkaZookeeper(String kafkaZookeeper) {
        this.kafkaZookeeper = kafkaZookeeper;
    }
}

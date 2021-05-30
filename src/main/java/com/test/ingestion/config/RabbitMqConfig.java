package com.test.ingestion.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {

    /** The host. */
    @Value("${RabbitMQ.Host}")
    private String host;

    /** The port. */
    @Value("${RabbitMQ.Port}")
    private Integer port;

    /** The username. */
    @Value("${RabbitMQ.User}")
    private String username;

    /** The pwd. */
    @Value("${RabbitMQ.Password}")
    private String password;

    /** The v host. */
    @Value("${RabbitMQ.VHost}")
    private String vHost;

    @Value("${RabbitMQ.prefatchCount: #{20}}")
    private int prefetchCount;

    @Value("${RabbitMQ.concurrentConsumers: #{4}}")
    private int concurrentConsumers;

    @Value("${RabbitMQ.subscriber.queue}")
    private String subscriberQueue;

    @Value("${RabbitMQ.subscriber.routingKey}")
    private String subscriberRoutingKey;

    @Value("${RabbitMQ.subscriber.exchange}")
    private String subscriberExchange;

    @Value("${RabbitMQ.subscriber.dlqQueue}")
    private String subscriberDLQQueue;

    @Value("${RabbitMQ.subscriber.dlqRoutingKey}")
    private String subscriberDLQRoutingKey;

    @Value("${RabbitMQ.subscriber.dlqExchange}")
    private String subscriberDLQExchange;

    @Value("${RabbitMQ.subscriber.dlqExchangeType}")
    private String subscriberDLQExchangeType;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getvHost() {
        return vHost;
    }

    public void setvHost(String vHost) {
        this.vHost = vHost;
    }

    public int getPrefetchCount() {
        return prefetchCount;
    }

    public void setPrefetchCount(int prefetchCount) {
        this.prefetchCount = prefetchCount;
    }

    public int getConcurrentConsumers() {
        return concurrentConsumers;
    }

    public void setConcurrentConsumers(int concurrentConsumers) {
        this.concurrentConsumers = concurrentConsumers;
    }

    public String getSubscriberQueue() {
        return subscriberQueue;
    }

    public void setSubscriberQueue(String subscriberQueue) {
        this.subscriberQueue = subscriberQueue;
    }

    public String getSubscriberRoutingKey() {
        return subscriberRoutingKey;
    }

    public void setSubscriberRoutingKey(String subscriberRoutingKey) {
        this.subscriberRoutingKey = subscriberRoutingKey;
    }

    public String getSubscriberExchange() {
        return subscriberExchange;
    }

    public void setSubscriberExchange(String subscriberExchange) {
        this.subscriberExchange = subscriberExchange;
    }

    public String getSubscriberDLQQueue() {
        return subscriberDLQQueue;
    }

    public void setSubscriberDLQQueue(String subscriberDLQQueue) {
        this.subscriberDLQQueue = subscriberDLQQueue;
    }

    public String getSubscriberDLQRoutingKey() {
        return subscriberDLQRoutingKey;
    }

    public void setSubscriberDLQRoutingKey(String subscriberDLQRoutingKey) {
        this.subscriberDLQRoutingKey = subscriberDLQRoutingKey;
    }

    public String getSubscriberDLQExchange() {
        return subscriberDLQExchange;
    }

    public void setSubscriberDLQExchange(String subscriberDLQExchange) {
        this.subscriberDLQExchange = subscriberDLQExchange;
    }

    public String getSubscriberDLQExchangeType() {
        return subscriberDLQExchangeType;
    }

    public void setSubscriberDLQExchangeType(String subscriberDLQExchangeType) {
        this.subscriberDLQExchangeType = subscriberDLQExchangeType;
    }
}

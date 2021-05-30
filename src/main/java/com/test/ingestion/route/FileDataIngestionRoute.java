package com.test.ingestion.route;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.ingestion.config.RabbitMqConfig;
import com.test.ingestion.exception.InvalidMessageFormatException;
import com.test.ingestion.config.AppProperties;
import com.test.ingestion.model.event.FileAvailableEvent;
import com.test.ingestion.service.FileCsvServiceImpl;
import org.apache.camel.Exchange;
import org.apache.camel.ExchangePattern;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.kafka.KafkaConstants;
import org.apache.camel.spring.SpringRouteBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class FileDataIngestionRoute extends SpringRouteBuilder {

    private static final Logger LOG = LoggerFactory.getLogger(FileDataIngestionRoute.class);

    @Autowired
    private AppProperties appProperties;

    @Autowired
    private RabbitMqConfig rabbitMqConfig;

    @Override
    public void configure() throws Exception {

        String topicName = "brokers="+appProperties.getKafkaBootstrapServer();
        String kafkaServer = "kafka:"+appProperties.getKafkaTopic();
        String zooKeeperHost = "zookeeperHost="+appProperties.getKafkaZookeeper()+"&zookeeperPort=2181";
        String serializerClass = "serializerClass=kafka.serializer.StringEncoder";
        String toKafka = new StringBuilder().append(kafkaServer).append("?").append(topicName)
                //.append("&")
                //.append(zooKeeperHost).append("&")
                //.append(serializerClass)
                .toString();


        from("direct:kafkaRoute").routeId(this.getClass().getName()).
//                .process(new Processor() {
//            @Override
//            public void process(Exchange exchange) throws Exception {
//                exchange.getIn().setHeader(KafkaConstants.PARTITION_KEY, 1);
//                exchange.getIn().setHeader(KafkaConstants.KEY, "1");
//            }
//        }).
                to(toKafka).log("on the partition ${headers[kafka.PARTITION]}").bean(kafkaOutputBean.class);

        from(buildRMQReceiverUrl()).routeId("fileAvailable").setExchangePattern(ExchangePattern.InOnly)
                .process(new Processor() {
                    @Override
                    public void process(Exchange exchange) throws InvalidMessageFormatException {
                        try {
                            String evtBody = exchange.getIn().getBody(String.class);
                            FileAvailableEvent fileAvailableEvent = null;
                            try {
                                fileAvailableEvent = new ObjectMapper().readValue(evtBody, FileAvailableEvent.class);
                            } catch (IOException e) {
                                log.debug("Event => {}", exchange.getIn());
                                log.error("Event mapping exception as: {}", e.getMessage());
                                throw new InvalidMessageFormatException("InvalidMessageFormatException: invalid message");
                            }
                            log.debug("parsing success");
                            exchange.getIn().setBody(fileAvailableEvent);
                        }catch (Exception jsonParseException){
                            log.debug("Event => {}", exchange.getIn());
                            log.error("Event parseing exception as: {}", jsonParseException.getMessage());
                            throw new InvalidMessageFormatException("Invalid message format received.");
                        }
                    }
                }).bean(FileCsvServiceImpl.class,"pushToTopic")
                .to("log:"+this.getClass().getName()+"?showAll=true&level=debug");

    }

    private String buildRMQReceiverUrl() {
        final StringBuffer endpoint =  new StringBuffer("rabbitmq://" + rabbitMqConfig.getHost() + ":" + rabbitMqConfig.getPort() + "/"
                + rabbitMqConfig.getSubscriberExchange()
                + "?exchangeType=topic"
                + "&username=" + rabbitMqConfig.getUsername()
                + "&password=" + rabbitMqConfig.getPassword()
                + "&autoDelete=false"
                + "&autoAck=false"
                + "&automaticRecoveryEnabled=false"
                + "&routingKey=" + rabbitMqConfig.getSubscriberRoutingKey()
                + "&queue=" + rabbitMqConfig.getSubscriberQueue()
                + "&vhost=" + rabbitMqConfig.getvHost()
                + "&concurrentConsumers="+rabbitMqConfig.getConcurrentConsumers()
                + "&prefetchEnabled=true"
              //  + "&declare=false"
                + "&prefetchCount=" + rabbitMqConfig.getPrefetchCount()
                + "&deadLetterExchange=" + rabbitMqConfig.getSubscriberDLQExchange()
                + "&deadLetterRoutingKey=" + rabbitMqConfig.getSubscriberRoutingKey()
                + "&deadLetterQueue=" + rabbitMqConfig.getSubscriberDLQQueue()
                + "&deadLetterExchangeType=" + rabbitMqConfig.getSubscriberDLQExchangeType());

        return endpoint.toString();
    }

    public static class kafkaOutputBean {
        public void printKafkaBody(String body) {
            LOG.info("KafkaBody result >>>>> " + body);
        }
    }
}

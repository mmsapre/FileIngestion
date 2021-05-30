package com.test.ingestion.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.ingestion.config.SpoolerTemplateConfig;
import com.test.ingestion.model.FieldRows;
import com.test.ingestion.model.event.DistributionEvent;
import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.component.kafka.KafkaConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomKafkaItemWriter implements ItemWriter<FieldRows> {

    private static final Logger logger = LoggerFactory.getLogger(CustomKafkaItemWriter.class);
    private ProducerTemplate producerTemplate;
    private CamelContext camelContext;
    private int partition;
    private String templateId;
    private String fileId;
    @Override
    public void write(List<? extends FieldRows> list) throws Exception {
        logger.info("Received fields");
        for (FieldRows fieldRows: list) {
            Map<String,String> fieldMaps=fieldRows.getPayload();
            fieldMaps.put("timestamp", Instant.now().toString());
            fieldMaps.put("source", this.templateId);
            fieldMaps.put("fileId", this.fileId);
            Map<String, String> headers = new HashMap<String, String>();
            headers.put("templateId", this.templateId);
            DistributionEvent distributionEvent=new DistributionEvent(headers, fieldMaps);
            ObjectMapper objectMapper=new ObjectMapper();
            String jsonRows=objectMapper.writeValueAsString(distributionEvent);
            Exchange exchange = camelContext.getEndpoint("direct:kafkaRoute").createExchange();
            exchange.getIn().setHeader(KafkaConstants.PARTITION_KEY, this.partition);
            exchange.getIn().setHeader(KafkaConstants.KEY, this.partition+"");
            exchange.getIn().setBody(jsonRows);
            Map<String,Object> headerMap=new HashMap<>();
            headerMap.put("templateId", this.templateId);
            headerMap.put("fileId", this.fileId);
            headerMap.put(KafkaConstants.PARTITION_KEY, this.partition);
            headerMap.put(KafkaConstants.KEY, this.partition+"");
            headerMap.put(KafkaConstants.KAFKA_RECORDMETA,true);
            exchange.getIn().setHeaders(headerMap);
            producerTemplate.send("direct:kafkaRoute",exchange);
        }

    }

    public ProducerTemplate getProducerTemplate() {
        return producerTemplate;
    }

    public void setProducerTemplate(ProducerTemplate producerTemplate) {
        this.producerTemplate = producerTemplate;
    }

    public CamelContext getCamelContext() {
        return camelContext;
    }

    public void setCamelContext(CamelContext camelContext) {
        this.camelContext = camelContext;
    }

    public int getPartition() {
        return partition;
    }

    public void setPartition(int partition) {
        this.partition = partition;
    }

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }
}

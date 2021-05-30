package com.test.ingestion.service;

import com.test.ingestion.config.AppProperties;
import com.test.ingestion.config.SpoolerTemplateConfig;
import com.test.ingestion.model.event.FileAvailableEvent;
import com.test.ingestion.util.FileIdGenerator;
import org.apache.camel.Body;
import org.apache.camel.Exchange;
import org.apache.camel.component.kafka.KafkaConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class FileCsvServiceImpl {

    private static final Logger logger = LoggerFactory.getLogger(FileCsvServiceImpl.class);

    @Autowired
    private AppProperties appProperties;

    @Autowired
    private SpoolerTemplateConfig spoolerTemplateConfig;


    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private Step step;


//    @Autowired
//    @Qualifier("readCSVFilesJob")
//    private Job job;


    private static final String OVERRIDDEN_BY_EXPRESSION = null;

    public void pushToTopic(final Exchange exchange, @Body FileAvailableEvent fileAvailableEvent) throws JobParametersInvalidException, JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException {
        logger.info("Called pushToTopic");

        SpoolerTemplateConfig.ReferenceFileConfig referenceFileConfig=spoolerTemplateConfig.getTemplateConfig().get(fileAvailableEvent.getTemplateId().toLowerCase());
        String partition=referenceFileConfig.getPartition();
        String  templateId=fileAvailableEvent.getTemplateId();
        templateId=templateId==null?"blmberg":templateId;
        exchange.getIn().setHeader(KafkaConstants.PARTITION_KEY, partition);
        exchange.getIn().setHeader(KafkaConstants.PARTITION, partition);
        FileIdGenerator fileIdGenerator=new FileIdGenerator(templateId.toLowerCase());
        String fileId=fileIdGenerator.createId();

        Job job=jobBuilderFactory.get(fileAvailableEvent.getFileName())
                .start(step)
                .build();
        JobParameters jobParameters = new JobParametersBuilder().addLong("time", System.currentTimeMillis())
                .addString("dest", fileAvailableEvent.getFileUri())
                .addString("templateId",templateId)
                .addString("partition", partition)
                .addString("fileId", fileId).toJobParameters();
        jobLauncher.run(job, jobParameters);

    }




}

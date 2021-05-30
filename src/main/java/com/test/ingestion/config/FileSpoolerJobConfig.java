package com.test.ingestion.config;

import com.test.ingestion.model.FieldRows;
import com.test.ingestion.service.CustomKafkaItemWriter;
import com.test.ingestion.service.FlexibleFieldSetMapper;
import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import java.util.Objects;

@Configuration
@EnableBatchProcessing
public class FileSpoolerJobConfig {

    private static final Logger logger = LoggerFactory.getLogger(FileSpoolerJobConfig.class);
    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;


    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private SpoolerTemplateConfig spoolerTemplateConfig;

    @Autowired
    private FlexibleFieldSetMapper flexibleFieldSetMapper;

    @Autowired
    private ResourceLoader resourceLoader;

    @Autowired
    private ProducerTemplate producerTemplate;

    @Autowired
    private CamelContext camelContext;


    @Bean
    public Step step(ItemReader<FieldRows>reader,CustomKafkaItemWriter customKafkaItemWriter){

        return stepBuilderFactory.get("step")
                .<FieldRows,FieldRows>chunk(40)
                .reader(reader)
                .writer(customKafkaItemWriter)
                .build();
    }

    @StepScope
    @Bean
    public CustomKafkaItemWriter customKafkaItemWriter(ProducerTemplate producerTemplate, CamelContext camelContext , @Value("#{jobParameters['templateId']}") String templateId ,@Value("#{jobParameters['partition']}") String partition,@Value("#{jobParameters['fileId']}") String fileId){
        CustomKafkaItemWriter customKafkaItemWriter=new CustomKafkaItemWriter();
        customKafkaItemWriter.setCamelContext(camelContext);
        int parId=partition==null?0:Integer.parseInt(partition);
        customKafkaItemWriter.setPartition(parId);
        customKafkaItemWriter.setFileId(fileId);
        customKafkaItemWriter.setTemplateId(templateId);
        customKafkaItemWriter.setProducerTemplate(producerTemplate);
        return customKafkaItemWriter;
    }


    public LineMapper<FieldRows> lineMapper(String templateId){

        SpoolerTemplateConfig.ReferenceFileConfig referenceFileConfig=spoolerTemplateConfig.getTemplateConfig().get(templateId);
        String delimitTemplate=referenceFileConfig.getTemplate();
        String template[]=delimitTemplate.split("\\|");


        DefaultLineMapper<FieldRows> customerLineMapper = new DefaultLineMapper<>();
        customerLineMapper.setFieldSetMapper(new FlexibleFieldSetMapper());
        DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
        lineTokenizer.setDelimiter(",");
        lineTokenizer.setNames(template);


        customerLineMapper.setLineTokenizer(lineTokenizer);
        customerLineMapper.setFieldSetMapper(flexibleFieldSetMapper);
        return customerLineMapper;
    }

    @StepScope
    @Bean
    public FlatFileItemReader<FieldRows> reader(@Value("#{jobParameters['dest']}") String dest,@Value("#{jobParameters['templateId']}") String templateId) {
        logger.info("Called reader");
        Resource fileSystemResource=null;
        if (Objects.nonNull(dest))
            fileSystemResource= new FileSystemResource(dest);
        else {
            fileSystemResource= new FileSystemResource(spoolerTemplateConfig.getDefaultFilePath());
        }
        templateId=templateId==null?"blmberg":templateId;
        SpoolerTemplateConfig.ReferenceFileConfig referenceFileConfig=spoolerTemplateConfig.getTemplateConfig().get(templateId.toLowerCase());
        String delimitTemplate=referenceFileConfig.getTemplate();
        String template[]=delimitTemplate.split("\\|");


        FlatFileItemReader<FieldRows> flatFileItemReader1=new FlatFileItemReaderBuilder<FieldRows>().name("csvReader")
                .resource(fileSystemResource)
                .delimited()
               // .delimiter(",")
                .names(template)
                .lineMapper(lineMapper(templateId.toLowerCase()))
                .fieldSetMapper(flexibleFieldSetMapper)
                .build();
        logger.info("Called end reader");
        return flatFileItemReader1;
    }

}

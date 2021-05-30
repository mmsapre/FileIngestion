package com.test.ingestion.config;

import com.mongodb.DB;
import com.test.ingestion.repository.support.MongoDbJobRepositoryFactoryBean;
import org.springframework.batch.core.configuration.annotation.BatchConfigurer;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.explore.support.JobExplorerFactoryBean;
import org.springframework.batch.core.explore.support.SimpleJobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.dao.ExecutionContextDao;
import org.springframework.batch.core.repository.dao.JobExecutionDao;
import org.springframework.batch.core.repository.dao.JobInstanceDao;
import org.springframework.batch.core.repository.dao.StepExecutionDao;
import org.springframework.batch.support.transaction.ResourcelessTransactionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;

import javax.annotation.PostConstruct;

@Component
public class MongoDbBatchConfigurer implements BatchConfigurer
{
    @Autowired
    private DB db;

    @Autowired
    private ExecutionContextDao mongoExecutionContextDao;

    @Autowired
    private JobExecutionDao mongoJobExecutionDao;

    @Autowired
    private JobInstanceDao mongoJobInstanceDao;

    @Autowired
    private StepExecutionDao mongoStepExecutionDao;

    private PlatformTransactionManager transactionManager = new ResourcelessTransactionManager();

    private JobRepository jobRepository;

    private JobLauncher jobLauncher;

    protected MongoDbBatchConfigurer() {}

    public MongoDbBatchConfigurer(DB db )
    {
        setDb(db);
    }

    public void setDb( DB db )
    {
        this.db = db;
    }

    @Override
    public JobRepository getJobRepository()
    {
        return jobRepository;
    }

    @Override
    public PlatformTransactionManager getTransactionManager()
    {
        return transactionManager;
    }

    @Override
    public JobLauncher getJobLauncher()
    {
        return jobLauncher;
    }

    @Bean
    public JobExplorer getJobExplorer() throws Exception {
        return new SimpleJobExplorer(
                mongoJobInstanceDao,
                mongoJobExecutionDao,
                mongoStepExecutionDao,
                mongoExecutionContextDao
        );
    }

    protected JobExplorer createJobExplorer() throws Exception {
       return new SimpleJobExplorer(mongoJobInstanceDao, mongoJobExecutionDao, mongoStepExecutionDao, mongoExecutionContextDao);
    }

    @PostConstruct
    public void initialize() throws Exception
    {
        this.jobRepository = createJobRepository();
        this.jobLauncher = createJobLauncher();
    }

    private JobLauncher createJobLauncher() throws Exception
    {
        SimpleJobLauncher jobLauncher = new SimpleJobLauncher();
        jobLauncher.setJobRepository(jobRepository);
        jobLauncher.afterPropertiesSet();
        return jobLauncher;
    }

    protected JobRepository createJobRepository() throws Exception
    {
        MongoDbJobRepositoryFactoryBean factory = new MongoDbJobRepositoryFactoryBean();
        factory.setDb(db);
        factory.afterPropertiesSet();
        return (JobRepository) factory.getObject();
    }
}

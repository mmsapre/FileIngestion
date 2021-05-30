package com.test.ingestion.repository.support;

import com.mongodb.DB;
import com.test.ingestion.incrementer.MongoDbValueIncrementerFactory;
import com.test.ingestion.incrementer.ValueIncrementerFactory;
import com.test.ingestion.repository.dao.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobInstance;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.repository.ExecutionContextSerializer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.dao.*;
import org.springframework.batch.core.repository.support.SimpleJobRepository;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

public class MongoDbJobRepositoryFactoryBean implements FactoryBean, InitializingBean
{

    protected static final Log logger = LogFactory.getLog(MongoDbJobRepositoryFactoryBean.class);

    private DB db;

    private String collectionPrefix = AbstractMongoDbDao.DEFAULT_COLLECTION_PREFIX;

    private ValueIncrementerFactory incrementerFactory;

    private int maxVarCharLength = AbstractMongoDbDao.DEFAULT_EXIT_MESSAGE_LENGTH;

    private ExecutionContextSerializer serializer;


    public void setSerializer( ExecutionContextSerializer serializer )
    {
        this.serializer = serializer;
    }

    public void setMaxVarCharLength( int maxVarCharLength )
    {
        this.maxVarCharLength = maxVarCharLength;
    }


    public void setDb( DB db )
    {
        this.db = db;
    }

    /** Sets the collection prefix for all the batch meta-data tables. */
    public void setCollectionPrefix( String collectionPrefix )
    {
        this.collectionPrefix = collectionPrefix;
    }

    @Override
    public void afterPropertiesSet() throws Exception
    {
        Assert.notNull(db, "db must not be null.");

        if( incrementerFactory == null )
        {
            incrementerFactory = new MongoDbValueIncrementerFactory(db);
        }

        if( serializer == null )
        {
            XStreamExecutionContextStringSerializer defaultSerializer = new XStreamExecutionContextStringSerializer();
            defaultSerializer.afterPropertiesSet();

            serializer = defaultSerializer;
        }
    }

    protected JobInstanceDao createJobInstanceDao() throws Exception
    {
        MongoDbJobInstanceDao dao = new MongoDbJobInstanceDao();
        dao.setPrefix(collectionPrefix);
        dao.setDb(db);
        dao.setJobIncrementer(incrementerFactory.getIncrementer(collectionPrefix
                + "Sequence" + JobInstance.class.getSimpleName()));
        dao.afterPropertiesSet();
        return dao;
    }

    protected JobExecutionDao createJobExecutionDao() throws Exception
    {
        MongoDbJobExecutionDao dao = new MongoDbJobExecutionDao();
        dao.setDb(db);
        dao.setPrefix(collectionPrefix);
        dao.setJobExecutionIncrementer(incrementerFactory.getIncrementer(collectionPrefix
                + "Sequence" + JobExecution.class.getSimpleName()));
        dao.setExitMessageLength(maxVarCharLength);
        dao.afterPropertiesSet();
        return dao;
    }

    protected StepExecutionDao createStepExecutionDao() throws Exception
    {
        MongoDbStepExecutionDao dao = new MongoDbStepExecutionDao();
        dao.setDb(db);
        dao.setPrefix(collectionPrefix);
        dao.setStepExecutionIncrementer(incrementerFactory.getIncrementer(collectionPrefix
                + "Sequence" + StepExecution.class.getSimpleName()));
        dao.setExitMessageLength(maxVarCharLength);
        dao.afterPropertiesSet();
        return dao;
    }

    protected ExecutionContextDao createExecutionContextDao() throws Exception
    {
        MongoDbExecutionContextDao dao = new MongoDbExecutionContextDao();
        dao.setDb(db);
        dao.setSerializer(serializer);
        dao.setPrefix(collectionPrefix);
        dao.afterPropertiesSet();
        return dao;
    }


    @Override
    public Class<JobRepository> getObjectType()
    {
        return JobRepository.class;
    }

    @Override
    public boolean isSingleton()
    {
        return true;
    }

    @Override
    public Object getObject() throws Exception
    {
        return getTarget();
    }

    private Object getTarget() throws Exception
    {
        return new SimpleJobRepository(createJobInstanceDao(), createJobExecutionDao(), createStepExecutionDao(),
                createExecutionContextDao());
    }
}

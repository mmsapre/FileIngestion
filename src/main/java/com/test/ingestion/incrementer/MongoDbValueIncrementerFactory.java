package com.test.ingestion.incrementer;

import com.mongodb.DB;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

/**
 * MongoDB implementation of the {@link ValueIncrementerFactory}
 * interface.
 */
@Component
public class MongoDbValueIncrementerFactory implements ValueIncrementerFactory
{
    /** The MongoDB database */
    //@Autowired
    private DB db;

    public MongoDbValueIncrementerFactory( DB db )
    {
        Assert.notNull(db, "db must not be null");
        this.db = db;
    }

    @Override
    public ValueIncrementer getIncrementer( String incrementerName )
    {
        Assert.notNull(incrementerName);
        MongoDbValueIncrementer incrementer = new MongoDbValueIncrementer(db, incrementerName);
        return incrementer;
    }
}

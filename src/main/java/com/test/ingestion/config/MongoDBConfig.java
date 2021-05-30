package com.test.ingestion.config;

import com.mongodb.DB;
import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;
import com.mongodb.WriteConcern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableConfigurationProperties
public class MongoDBConfig {

    @Autowired
    private AppProperties appProperties;

    @Value("${spring.data.mongodb.host}")
    private String mongoHost;
    @Value("${spring.data.mongodb.port}")
    private String mongoPort;
    @Value("${spring.data.mongodb.username}")
    private String mongoUser;
    @Value("${spring.data.mongodb.password}")
    private String mongoPasswd;
    @Value("${spring.data.mongodb.database}")
    private String mongoDb;

    @Bean
    DB database() throws UnknownHostException
    {
        List<ServerAddress> seeds = new ArrayList<>();
        seeds.add(new ServerAddress(mongoHost,Integer.parseInt(mongoPort)));
        MongoClient mongoClient1 = new MongoClient(mongoHost, Integer.parseInt(mongoPort));
        mongoClient1.setWriteConcern(WriteConcern.ACKNOWLEDGED);
        return mongoClient1.getDB(mongoDb);    }
}

package io.github.jokoframework.wilson.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;

import com.mongodb.MongoClient;

/**
 * Spring MongoDB configuration file
 *
 */
@Configuration
public class SpringMongoConfig{

    @Value("${wilson.mongodb.database.host}")
    private String dbHost;
    @Value("${wilson.mongodb.database.port}")
    private Integer dbPort;
    @Value("${wilson.mongodb.database.name}")
    private String dbName;

    public @Bean
    MongoTemplate mongoTemplate() {

        return new MongoTemplate(new MongoClient(dbHost, dbPort), dbName);

    }

}

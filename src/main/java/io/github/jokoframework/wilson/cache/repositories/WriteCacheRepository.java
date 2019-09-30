package io.github.jokoframework.wilson.cache.repositories;

import io.github.jokoframework.wilson.cache.entities.WriteCacheEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface WriteCacheRepository extends MongoRepository<WriteCacheEntity, String> {

    @Query(value = "{'state': ?0}")
    public List<WriteCacheEntity> findAllByState(String state);

}
package io.github.jokoframework.wilson.cache.repositories;

import io.github.jokoframework.wilson.cache.entities.WriteOperationEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface WriteOperationRepository extends MongoRepository<WriteOperationEntity, String> {

    public Optional<WriteOperationEntity> findByResource(String resource);

}
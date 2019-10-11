package io.github.jokoframework.wilson.cache.repositories;

import io.github.jokoframework.wilson.cache.entities.ReadOperationEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface ReadOperationRepository extends MongoRepository<ReadOperationEntity, String> {

    public Optional<ReadOperationEntity> findByResource(String resource);

}

package io.github.jokoframework.wilson.cache.service;

import io.github.jokoframework.wilson.cache.entities.ReadOperationEntity;
import io.github.jokoframework.wilson.cache.entities.ResponseCacheEntity;
import io.github.jokoframework.wilson.exceptions.ReadOperationException;

import java.util.List;

public interface ReadOperationService {

    // ReadOperation services
    public void insertReadOperation(ReadOperationEntity readOperationEntity);

    public List<ReadOperationEntity> listReadOperations();

    // Insert Test Data for a GET request
    public void updateReadOperationCache(String service) throws ReadOperationException;

}

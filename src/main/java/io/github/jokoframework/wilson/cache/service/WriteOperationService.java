package io.github.jokoframework.wilson.cache.service;

import io.github.jokoframework.wilson.cache.entities.WriteOperationEntity;

import java.util.List;

public interface WriteOperationService {

    // Write Operation Services
    public void insertWriteOperation(WriteOperationEntity writeOperationEntity);

    public List<WriteOperationEntity> listWriteOperations();

}

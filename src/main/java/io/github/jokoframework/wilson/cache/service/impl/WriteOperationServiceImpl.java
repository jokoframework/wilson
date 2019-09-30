package io.github.jokoframework.wilson.cache.service.impl;

import io.github.jokoframework.wilson.cache.entities.WriteOperationEntity;
import io.github.jokoframework.wilson.cache.repositories.WriteOperationRepository;
import io.github.jokoframework.wilson.cache.service.WriteOperationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WriteOperationServiceImpl implements WriteOperationService {

    @Autowired
    private WriteOperationRepository writeOperationRepository;

    public void insertWriteOperation(WriteOperationEntity writeOperationEntity) {
        writeOperationRepository.insert(writeOperationEntity);
    }

    public List<WriteOperationEntity> listWriteOperations() {
        return writeOperationRepository.findAll();
    }

}

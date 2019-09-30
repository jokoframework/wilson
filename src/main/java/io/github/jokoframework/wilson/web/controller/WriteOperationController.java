package io.github.jokoframework.wilson.web.controller;

import io.github.jokoframework.wilson.cache.entities.WriteOperationEntity;
import io.github.jokoframework.wilson.cache.service.WriteOperationService;
import io.github.jokoframework.wilson.constants.ApiPaths;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class WriteOperationController {

    private final WriteOperationService writeOperationService;

    @Autowired
    public WriteOperationController(WriteOperationService writeOperationService){
        this.writeOperationService = writeOperationService;
    }

    // Write Operation Endpoints

    @PostMapping(value = ApiPaths.WILSON_INSERT_WRITE_OPERATION)
    public void insertReadOperation(@RequestBody WriteOperationEntity writeOperationEntity){
        writeOperationService.insertWriteOperation(writeOperationEntity);
    }

    @GetMapping(value = ApiPaths.WILSON_LIST_WRITE_OPERATION)
    public List<WriteOperationEntity> listWriteOperations(){
        return writeOperationService.listWriteOperations();
    }

}

package io.github.jokoframework.wilson.web.controller;

import io.github.jokoframework.wilson.cache.entities.ReadOperationEntity;
import io.github.jokoframework.wilson.cache.service.ReadOperationService;
import io.github.jokoframework.wilson.constants.ApiPaths;
import io.github.jokoframework.wilson.exceptions.ReadOperationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ReadOperationController {

    private final ReadOperationService readOperationService;

    @Autowired
    public ReadOperationController(ReadOperationService readOperationService){
        this.readOperationService=readOperationService;
    }

    @PostMapping(value = ApiPaths.WILSON_INSERT_READ_OPERATION)
    public void insertReadOperation(@RequestBody ReadOperationEntity readOperationEntity){
        readOperationService.insertReadOperation(readOperationEntity);
    }

    @GetMapping(value = ApiPaths.WILSON_LIST_READ_OPERATION)
    public List<ReadOperationEntity> listReadOperations(){
        return readOperationService.listReadOperations();
    }

}

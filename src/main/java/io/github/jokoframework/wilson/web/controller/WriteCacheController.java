package io.github.jokoframework.wilson.web.controller;

import io.github.jokoframework.wilson.cache.entities.WriteCacheEntity;
import io.github.jokoframework.wilson.cache.enums.WriteCacheStateEnum;
import io.github.jokoframework.wilson.cache.service.WriteCacheService;
import io.github.jokoframework.wilson.constants.ApiPaths;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class WriteCacheController {

    private final WriteCacheService writeCacheService;

    @Autowired
    public WriteCacheController(WriteCacheService writeCacheService){
        this.writeCacheService = writeCacheService;
    }

    @GetMapping(value = ApiPaths.WILSON_LIST_WRITE_CACHE)
    public List<WriteCacheEntity> listWriteCachesByState(@RequestParam(value = "state") WriteCacheStateEnum state){
        return writeCacheService.listWriteCachesByState(state);
    }

}

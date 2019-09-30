package io.github.jokoframework.wilson.web.controller;

import io.github.jokoframework.wilson.cache.service.WilsonMasterService;
import io.github.jokoframework.wilson.constants.ApiPaths;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class WilsonMasterController {

    private final WilsonMasterService wilsonMasterService;

    @Autowired
    public WilsonMasterController(WilsonMasterService wilsonMasterService){
        this.wilsonMasterService=wilsonMasterService;
    }

    @GetMapping(value = ApiPaths.WILSON_MASTER)
    public ResponseEntity<String> wilsonMasterGetRequest(@RequestParam(value = "resource") String resource){
        return wilsonMasterService.processGetRequest(resource);
    }

    @PostMapping(value = ApiPaths.WILSON_MASTER)
    public ResponseEntity<String> wilsonMasterPostRequest(@RequestParam(value = "resource") String resource,
                                         HttpEntity<String> request){
        return wilsonMasterService.processPostRequest(resource, request);
    }
}

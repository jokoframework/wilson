package io.github.jokoframework.wilson.cache.service;

import org.springframework.http.ResponseEntity;

public interface WilsonMasterService {

    // Process GET requests from the Frontend, applying Cache functions if available
    public ResponseEntity<Object> processGetRequest(String resource);

}

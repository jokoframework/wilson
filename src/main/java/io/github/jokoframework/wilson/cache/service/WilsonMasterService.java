package io.github.jokoframework.wilson.cache.service;

import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;

public interface WilsonMasterService {

    // Process GET requests from the Frontend, applying Cache functions if available
    public ResponseEntity<String> processGetRequest(String resource);

    // Process POST requests from the Frontend, applying Cache functions if available
    public ResponseEntity<String> processPostRequest(String resource, HttpEntity<String> request);

}

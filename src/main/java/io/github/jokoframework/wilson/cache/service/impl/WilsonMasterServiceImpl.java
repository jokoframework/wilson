package io.github.jokoframework.wilson.cache.service.impl;

import io.github.jokoframework.wilson.cache.entities.*;
import io.github.jokoframework.wilson.cache.enums.WriteHttpVerbEnum;
import io.github.jokoframework.wilson.cache.enums.WriteCacheStateEnum;
import io.github.jokoframework.wilson.cache.repositories.ReadOperationRepository;
import io.github.jokoframework.wilson.cache.repositories.WriteOperationRepository;
import io.github.jokoframework.wilson.cache.service.ReadOperationService;
import io.github.jokoframework.wilson.cache.service.WilsonMasterService;
import io.github.jokoframework.wilson.cache.service.WriteCacheService;
import io.github.jokoframework.wilson.scheduler.ScheduledTasks;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Service
public class WilsonMasterServiceImpl implements WilsonMasterService {

    private static final Logger LOGGER = LoggerFactory.getLogger(WilsonMasterServiceImpl.class);

    @Autowired
    private ReadOperationRepository readOperationRepository;

    @Autowired
    private WriteOperationRepository writeOperationRepository;

    @Autowired
    private ReadOperationService readOperationService;

    @Autowired
    private WriteCacheService writeCacheService;

    @Value("${wilson.backend.base.url}")
    private String baseUrl;

    public ResponseEntity<String> processGetRequest(String resource) {
        // The GET request to the resource is called
        // TODO Add logic to first try to obtain a fresh response and return that!

        // (Failed to get fresh response)
        // Lookup if GET requests to the resource are being Cached
        Optional<ReadOperationEntity> readOperationEntity = readOperationRepository.findByResource(resource);

        // Return initial request response if Cache functionality is not available for the resource
        // TODO If there is a Cache entry it must be checked that its not stale!
        if (readOperationEntity.isEmpty() || readOperationEntity.get().getResponseCache() == null) {
            LOGGER.info("Could not execute GET request and there was no Cache available");
            return new ResponseEntity<>("Could not execute GET request and there was no Cache available", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        // Cache is available and is fresh
        ResponseCacheEntity responseCacheEntity = readOperationEntity.get().getResponseCache();
        return new ResponseEntity<>(responseCacheEntity.getBody(), responseCacheEntity.getHeaders(), responseCacheEntity.getStatusCode());
    }

    // TODO Improve Try Catch error handling and create/add method's throwable Exceptions
    public ResponseEntity<String> processPostRequest(String resource, HttpEntity<String> request) {
        // The POST request to the resource is called
        HttpHeaders headers = new HttpHeaders();
        headers.addAll(request.getHeaders());
        if (ScheduledTasks.getAccessToken() != null) {
            headers.set("X-JOKO-AUTH", ScheduledTasks.getAccessToken());
        }

        request = new HttpEntity<>(request.getBody(), headers);
        RestTemplate restTemplate = new RestTemplate();
        try {
            return restTemplate.exchange(baseUrl + resource,
                    HttpMethod.POST,
                    request,
                    String.class);
        } catch (ResourceAccessException e) {
            // (Failed to access server, no connection)
            // Lookup if POST requests to the resource are being Cached
            Optional<WriteOperationEntity> writeOperationEntity = writeOperationRepository.findByResource(resource);

            // If not, return error to user
            if (writeOperationEntity.isEmpty()) {
                return new ResponseEntity<>("Could not contact the server and the resource is not being cached", HttpStatus.INTERNAL_SERVER_ERROR);
            }

            // If it is, store Write Cache for later execution and return success to user with explanation
            WriteCacheEntity writeCache = new WriteCacheEntity();
            writeCache.setResource(resource);
            writeCache.setRequestType(WriteHttpVerbEnum.POST);
            writeCache.setState(WriteCacheStateEnum.AWAITING);
            writeCache.setRequest(new RequestCacheEntity(request));
            writeCacheService.insertWriteCache(writeCache);

            return new ResponseEntity<>("Could not contact the server but the request was cached", HttpStatus.OK);
        }
    }
}

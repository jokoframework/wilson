package io.github.jokoframework.wilson.cache.service.impl;

import io.github.jokoframework.wilson.cache.entities.ReadCacheEntity;
import io.github.jokoframework.wilson.cache.entities.ReadOperationEntity;
import io.github.jokoframework.wilson.cache.repositories.ReadOperationRepository;
import io.github.jokoframework.wilson.cache.service.ReadOperationService;
import io.github.jokoframework.wilson.exceptions.ReadOperationException;
import io.github.jokoframework.wilson.mapper.OrikaBeanMapper;
import io.github.jokoframework.wilson.scheduler.ScheduledTasks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ReadOperationServiceImpl implements ReadOperationService {

    @Autowired
    private ReadOperationRepository readRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private OrikaBeanMapper mapper;

    // TODO CREATE APPLICATION CONSTANTS
    @Value("${wilson.backend.base.url}")
    private String baseUrl;

    @Override
    public void insertReadOperation(ReadOperationEntity readOperation) {
        readRepository.insert(readOperation);
    }

    @Override
    public List<ReadOperationEntity> listReadOperations() {
        return readRepository.findAll();
    }

    public void updateReadOperationCache(String resource) throws ReadOperationException{
        // A Read Operation is obtained with the resource field (Unique value)
        Optional<ReadOperationEntity> readOperationEntity = readRepository.findByResource(resource);
        if (readOperationEntity.isEmpty()) {
            throw ReadOperationException.notFound(resource);
        }

        // The request defined in the Read Operation is built and called
        HttpHeaders headers = new HttpHeaders();

        // TODO MOVE INJECTING THE SECRET INTO HEADER TO A RestTemplate INTERCEPTOR
        if (ScheduledTasks.getAccessToken() != null) {
            headers.add("X-JOKO-AUTH", ScheduledTasks.getAccessToken());
        }

        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>(headers);
        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<String> response = restTemplate.exchange(baseUrl + resource,
                HttpMethod.GET,
                request,
                String.class);

        // A Read Cache is built using the Response and updates the Read Operation's "responseCache" field
        ReadCacheEntity newCache = new ReadCacheEntity();
        newCache.setHeaders(response.getHeaders());
        newCache.setBody(response.getBody());
        newCache.setStatusCode(response.getStatusCode());

        saveReadCache(readOperationEntity.get().getResource(), newCache);
    }

    // Pushes a new entry into the cache list of an operation
    public void saveReadCache(String resource, ReadCacheEntity readCacheEntity) {
        readCacheEntity.setTimeOfArrival(LocalDateTime.now());
        mongoTemplate.updateFirst(
                Query.query(Criteria.where("resource").is(resource)),
                new Update().set("responseCache", readCacheEntity),
                ReadOperationEntity.class);
    }
}

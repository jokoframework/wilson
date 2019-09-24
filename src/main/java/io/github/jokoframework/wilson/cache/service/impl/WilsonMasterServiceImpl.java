package io.github.jokoframework.wilson.cache.service.impl;

import io.github.jokoframework.wilson.cache.entities.ReadCacheEntity;
import io.github.jokoframework.wilson.cache.entities.ReadOperationEntity;
import io.github.jokoframework.wilson.cache.repositories.ReadOperationRepository;
import io.github.jokoframework.wilson.cache.service.ReadOperationService;
import io.github.jokoframework.wilson.cache.service.WilsonMasterService;
import io.github.jokoframework.wilson.mapper.OrikaBeanMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class WilsonMasterServiceImpl implements WilsonMasterService {

    private static final Logger LOGGER = LoggerFactory.getLogger(WilsonMasterServiceImpl.class);

    @Autowired
    private ReadOperationRepository readRepository;

    @Autowired
    ReadOperationService readOperationService;

    @Autowired
    private OrikaBeanMapper mapper;

    public ResponseEntity<Object> processGetRequest(String resource) {
        // A Read Operation is obtained with the resource (Unique value)
        Optional<ReadOperationEntity> readOperationEntity = readRepository.findByResource(resource);

        // If an entry was found and has a Cache entry it will return that otherwise it will try to call the request
        // right now, update the cache as required and return the response
        if (readOperationEntity.isPresent() && readOperationEntity.get().getResponseCache() != null) {
            ReadCacheEntity readCacheEntity = readOperationEntity.get().getResponseCache();
            return new ResponseEntity<>(readCacheEntity.getBody(), readCacheEntity.getHeaders(), readCacheEntity.getStatusCode());
        }

        // TODO IMPLEMENT TRYING TO OBTAIN A RESPONSE FROM THE ORIGIN WHEN IT HAS NO CACHE (WETHER HE TRIES TO STORE IT OR NOT!)
        LOGGER.info("Wilson did not find the Read Operation and still can't call the endpoint in this scenario");
        return new ResponseEntity<>("Wilson did not find the Read Operation and still can't call the endpoint in this scenario", HttpStatus.NO_CONTENT);
    }
}

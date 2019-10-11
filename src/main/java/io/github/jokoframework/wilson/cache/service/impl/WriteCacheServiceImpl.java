package io.github.jokoframework.wilson.cache.service.impl;

import io.github.jokoframework.wilson.cache.entities.ResponseCacheEntity;
import io.github.jokoframework.wilson.cache.entities.WriteCacheEntity;
import io.github.jokoframework.wilson.cache.enums.WriteCacheStateEnum;
import io.github.jokoframework.wilson.cache.repositories.WriteCacheRepository;
import io.github.jokoframework.wilson.cache.service.WriteCacheService;
import io.github.jokoframework.wilson.scheduler.ScheduledTasks;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class WriteCacheServiceImpl implements WriteCacheService {

    @Autowired
    private WriteCacheRepository writeCacheRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Value("${wilson.backend.base.url}")
    private String baseUrl;

    @Override
    public void insertWriteCache(WriteCacheEntity writeCacheEntity) {
        writeCacheRepository.insert(writeCacheEntity);
    }

    @Override
    public void executeWriteCache(WriteCacheEntity writeCacheEntity) {
        // A Write Cache's request is built and called
        HttpHeaders headers = new HttpHeaders();

        if (ScheduledTasks.getAccessToken() != null) {
            headers.set("X-JOKO-AUTH", ScheduledTasks.getAccessToken());
        }
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> request = new HttpEntity<>(writeCacheEntity.getRequest().getBody(), headers);
        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<String> response = restTemplate.exchange(baseUrl + writeCacheEntity.getResource(),
                HttpMethod.POST,
                request,
                String.class);

        ResponseCacheEntity responseCacheEntity = new ResponseCacheEntity(response);

        // Successful response is stored in Cache entry, Cache state is set to SUCCESS
        updateWriteCacheResponse(writeCacheEntity.getId(), responseCacheEntity, WriteCacheStateEnum.SUCCESS);
    }


    public void updateWriteCacheResponse(ObjectId id, ResponseCacheEntity responseCacheEntity, WriteCacheStateEnum state) {
        mongoTemplate.updateFirst(
                Query.query(Criteria.where("_id").is(id)),
                new Update().set("state", state)
                            .set("response", responseCacheEntity),
                WriteCacheEntity.class);
    }

    @Override
    public List<WriteCacheEntity> listWriteCachesByState(WriteCacheStateEnum state) {
        return writeCacheRepository.findAllByState(state.toString());
    }
}

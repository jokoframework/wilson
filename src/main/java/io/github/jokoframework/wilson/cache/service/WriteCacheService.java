package io.github.jokoframework.wilson.cache.service;

import io.github.jokoframework.wilson.cache.entities.ResponseCacheEntity;
import io.github.jokoframework.wilson.cache.entities.WriteCacheEntity;
import io.github.jokoframework.wilson.cache.enums.WriteCacheStateEnum;
import org.bson.types.ObjectId;

import java.util.List;

public interface WriteCacheService {

    public void insertWriteCache(WriteCacheEntity writeCacheEntity);

    public List<WriteCacheEntity> listWriteCachesByState(WriteCacheStateEnum state);

    public void executeWriteCache(WriteCacheEntity writeCacheEntity);

    public void updateWriteCacheResponse(ObjectId id, ResponseCacheEntity responseCacheEntity, WriteCacheStateEnum state);

}

package io.github.jokoframework.wilson.cache.entities;

import io.github.jokoframework.wilson.cache.enums.ReadOperationHttpVerbEnum;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;
import java.io.Serializable;

@Document("readOperation")
public class ReadOperationEntity implements Serializable {
    @Id
    private ObjectId id;
    @Indexed(unique = true)
    private String resource;
    private ReadOperationHttpVerbEnum requestType;
    private Integer retryTimer;
    private Integer maxAge;
    private ReadCacheEntity responseCache;

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getResource() {
        return resource;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }

    public ReadOperationHttpVerbEnum getRequestType() {
        return requestType;
    }

    public void setRequestType(ReadOperationHttpVerbEnum requestType) {
        this.requestType = requestType;
    }

    public Integer getRetryTimer() {
        return retryTimer;
    }

    public void setRetryTimer(Integer retryTimer) {
        this.retryTimer = retryTimer;
    }

    public Integer getMaxAge() {
        return maxAge;
    }

    public void setMaxAge(Integer maxAge) {
        this.maxAge = maxAge;
    }

    public ReadCacheEntity getResponseCache() {
        return responseCache;
    }

    public void setResponseCache(ReadCacheEntity responseCache) {
        this.responseCache = responseCache;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", id)
                .append("resource", resource)
                .append("requestType", requestType)
                .append("retryTimer", retryTimer)
                .append("maxAge", maxAge)
                .append("responseCache", responseCache)
                .toString();
    }
}

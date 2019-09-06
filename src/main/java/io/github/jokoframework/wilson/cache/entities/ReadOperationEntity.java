package io.github.jokoframework.wilson.cache.entities;

import io.github.jokoframework.wilson.cache.enums.ReadOperationHttpVerbEnum;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.persistence.Id;
import java.io.Serializable;

@Document("readOperation")
public class ReadOperationEntity implements Serializable {
    @Id
    @Field("_id")
    private String uri;
    private ReadOperationHttpVerbEnum requestType;
    private Integer retryTimer;
    private Integer maxAge;
    private ReadCacheEntity responseCache;

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
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
                .appendSuper(super.toString())
                .append("uri", uri)
                .append("requestType", requestType)
                .append("retryTimer", retryTimer)
                .append("maxAge", maxAge)
                .append("responseCache", responseCache)
                .toString();
    }
}

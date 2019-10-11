package io.github.jokoframework.wilson.cache.entities;

import io.github.jokoframework.wilson.cache.enums.WriteHttpVerbEnum;
import io.github.jokoframework.wilson.cache.enums.WriteCacheStateEnum;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;
import java.io.Serializable;

@Document("writeCache")
public class WriteCacheEntity implements Serializable {
    @Id
    private ObjectId id;
    private String resource;
    private WriteHttpVerbEnum requestType;
    private WriteCacheStateEnum state;
    private RequestCacheEntity request;
    private ResponseCacheEntity response;

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

    public WriteHttpVerbEnum getRequestType() {
        return requestType;
    }

    public void setRequestType(WriteHttpVerbEnum requestType) {
        this.requestType = requestType;
    }

    public WriteCacheStateEnum getState() {
        return state;
    }

    public void setState(WriteCacheStateEnum state) {
        this.state = state;
    }

    public RequestCacheEntity getRequest() {
        return request;
    }

    public void setRequest(RequestCacheEntity request) {
        this.request = request;
    }

    public ResponseCacheEntity getResponse() {
        return response;
    }

    public void setResponse(ResponseCacheEntity response) {
        this.response = response;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", id)
                .append("resource", resource)
                .append("requestType", requestType)
                .append("state", state)
                .append("request", request)
                .append("response", response)
                .toString();
    }
}

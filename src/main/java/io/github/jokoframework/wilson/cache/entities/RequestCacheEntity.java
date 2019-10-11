package io.github.jokoframework.wilson.cache.entities;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;

import java.io.Serializable;

public class RequestCacheEntity implements Serializable {
    private String body;
    private HttpHeaders headers;

    public RequestCacheEntity() {}
    public RequestCacheEntity(HttpEntity<String> requestEntity) {
        this.body = requestEntity.getBody();
        this.headers = requestEntity.getHeaders();
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public HttpHeaders getHeaders() {
        return headers;
    }

    public void setHeaders(HttpHeaders headers) {
        this.headers = headers;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("body", body)
                .append("headers", headers)
                .toString();
    }
}

package io.github.jokoframework.wilson.cache.entities;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.Serializable;

public class ResponseCacheEntity implements Serializable {
    private String body;
    private HttpHeaders headers;
    private HttpStatus statusCode;
    private int statusCodeValue;

    public ResponseCacheEntity() {}
    public ResponseCacheEntity(ResponseEntity<String> responseEntity) {
        this.body = responseEntity.getBody();
        this.headers = responseEntity.getHeaders();
        this.statusCode = responseEntity.getStatusCode();
        this.statusCodeValue = responseEntity.getStatusCodeValue();
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

    public HttpStatus getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(HttpStatus statusCode) {
        this.statusCode = statusCode;
    }

    public int getStatusCodeValue() {
        return statusCodeValue;
    }

    public void setStatusCodeValue(int statusCodeValue) {
        this.statusCodeValue = statusCodeValue;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("body", body)
                .append("headers", headers)
                .append("statusCode", statusCode)
                .append("statusCodeValue", statusCodeValue)
                .toString();
    }
}

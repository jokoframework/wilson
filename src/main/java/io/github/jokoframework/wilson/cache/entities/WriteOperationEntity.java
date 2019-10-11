package io.github.jokoframework.wilson.cache.entities;

import io.github.jokoframework.wilson.cache.enums.WriteHttpVerbEnum;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;
import java.io.Serializable;

@Document("writeOperation")
public class WriteOperationEntity implements Serializable {
    @Id
    private ObjectId id;
    @Indexed(unique = true)
    private String resource;
    private WriteHttpVerbEnum requestType;

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

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", id)
                .append("resource", resource)
                .append("requestType", requestType)
                .toString();
    }
}

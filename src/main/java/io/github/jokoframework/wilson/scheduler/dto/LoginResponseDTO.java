package io.github.jokoframework.wilson.scheduler.dto;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.Date;

public class LoginResponseDTO {
    private String success;
    private String errorCode;
    private String message;
    private String secret;
    private Date expiration;

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public Date getExpiration() {
        return expiration;
    }

    public void setExpiration(Date expiration) {
        this.expiration = expiration;
    }


    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("success", success)
                .append("errorCode", errorCode)
                .append("message", message)
                .append("secret", secret)
                .append("expiration", expiration)
                .toString();
    }
}

package com.estudo.dscatalog.exception.error;

import java.time.Instant;

public class CustomError {

    private Instant timestamp;
    private Integer status;
    private String message;
    private String error;
    private String path;

    public CustomError(){

    }

    public CustomError(Instant timestamp, Integer status,  String error, String path) {
        this.timestamp = timestamp;
        this.status = status;
        this.error = error;
        this.path = path;
    }

    public CustomError(Instant timestamp, Integer status, String message, String error, String path) {
        this.timestamp = timestamp;
        this.status = status;
        this.message = message;
        this.error = error;
        this.path = path;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public Integer getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public String getError() {
        return error;
    }

    public String getPath() {
        return path;
    }
}

package com.estudo.dscatalog.exception.error;

public class FieldMessage {

    private String fieldError;
    private String message;

    public FieldMessage(){

    }

    public FieldMessage(String fieldError, String message) {
        this.fieldError = fieldError;
        this.message = message;
    }

    public String getFieldError() {
        return fieldError;
    }

    public String getMessage() {
        return message;
    }
}

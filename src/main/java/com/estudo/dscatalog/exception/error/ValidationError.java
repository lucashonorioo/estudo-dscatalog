package com.estudo.dscatalog.exception.error;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class ValidationError extends CustomError{

    List<FieldMessage> erors = new ArrayList<>();

    public ValidationError(Instant timestamp, Integer status, String error, String path) {
        super(timestamp, status, error, path);
    }

    public List<FieldMessage> getErors() {
        return erors;
    }
    public void addError(String fieldName, String message){
        erors.add(new FieldMessage(fieldName, message));
    }
}

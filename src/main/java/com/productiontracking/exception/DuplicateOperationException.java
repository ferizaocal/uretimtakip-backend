package com.productiontracking.exception;

public class DuplicateOperationException extends RuntimeException {

    public DuplicateOperationException(String message) {
        super("Duplicate Operation: " + message);
    }

}

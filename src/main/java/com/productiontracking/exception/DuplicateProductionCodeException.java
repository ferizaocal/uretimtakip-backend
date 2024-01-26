package com.productiontracking.exception;

public class DuplicateProductionCodeException extends RuntimeException {

    public DuplicateProductionCodeException(String message) {
        super("Duplicate Production Code: " + message);
    }
}

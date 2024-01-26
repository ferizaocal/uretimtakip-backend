package com.productiontracking.exception;

public class DuplicateProductionModelException extends RuntimeException {

    public DuplicateProductionModelException(String message) {
        super("Duplicate Production Model: " + message);
    }
}

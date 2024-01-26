package com.productiontracking.exception;

public class DuplicateRoleException extends RuntimeException {

    public DuplicateRoleException(String message) {
        super("Duplicate role: " + message);
    }
}

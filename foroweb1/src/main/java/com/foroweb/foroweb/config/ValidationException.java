package com.foroweb.foroweb.config;

import java.util.Map;

public class ValidationException extends RuntimeException {
    private Map<String, String> errors;

    public ValidationException(Map<String, String> errors) {
        super("Validation errors");
        this.errors = errors;
    }

    public Map<String, String> getErrors() {
        return errors;
    }

    public Map<String, String> getValidationErrors() {
        return errors;
    }
}

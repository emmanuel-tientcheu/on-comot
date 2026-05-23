package com.example.comot.auth.application.exceptions;

public class NotFoundException extends RuntimeException {
    public NotFoundException(String entity, String key) {
        super(
                String.format(
                        "%s with the key %s was not found", entity, key
                )
        );
    }
}

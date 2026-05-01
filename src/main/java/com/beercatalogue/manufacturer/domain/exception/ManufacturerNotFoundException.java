package com.beercatalogue.manufacturer.domain.exception;

public class ManufacturerNotFoundException extends RuntimeException {
    public ManufacturerNotFoundException(String message) {
        super(message);
    }
}

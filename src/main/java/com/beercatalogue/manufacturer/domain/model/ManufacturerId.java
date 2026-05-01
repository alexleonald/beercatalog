package com.beercatalogue.manufacturer.domain.model;

import com.fasterxml.jackson.annotation.JsonValue;

import java.util.UUID;

public record ManufacturerId(@JsonValue UUID value) {

    public static ManufacturerId generate() {
        return new ManufacturerId(UUID.randomUUID());
    }

    public static ManufacturerId of(UUID value) {
        return new ManufacturerId(value);
    }
}

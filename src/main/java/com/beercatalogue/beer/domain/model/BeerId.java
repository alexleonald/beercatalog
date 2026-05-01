package com.beercatalogue.beer.domain.model;

import com.fasterxml.jackson.annotation.JsonValue;

import java.util.UUID;

public record BeerId(@JsonValue UUID value) {

    public static BeerId generate() {
        return new BeerId(UUID.randomUUID());
    }

    public static BeerId of(UUID value) {
        return new BeerId(value);
    }
}

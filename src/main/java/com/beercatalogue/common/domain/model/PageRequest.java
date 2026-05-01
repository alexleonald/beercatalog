package com.beercatalogue.common.domain.model;

public record PageRequest(
        Integer page,
        Integer size,
        String sortBy,
        String direction
) {
}
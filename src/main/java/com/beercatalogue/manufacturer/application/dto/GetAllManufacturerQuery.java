package com.beercatalogue.manufacturer.application.dto;

public record GetAllManufacturerQuery(
        Integer page,
        Integer size,
        String sortBy,
        String direction
) {
    public static GetAllManufacturerQuery of(Integer page, Integer size, String sortBy, String direction) {
        return new GetAllManufacturerQuery(
                page != null ? page : 0,
                size != null ? size : 20,
                sortBy != null ? sortBy : "name",
                direction != null ? direction : "asc"
        );
    }
}

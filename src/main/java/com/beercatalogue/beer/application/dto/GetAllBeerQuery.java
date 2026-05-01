package com.beercatalogue.beer.application.dto;

public record GetAllBeerQuery(
        Integer page,
        Integer size,
        String sortBy,
        String direction
) {
    public static GetAllBeerQuery of(Integer page, Integer size, String sortBy, String direction) {
        return new GetAllBeerQuery(
                page != null ? page : 0,
                size != null ? size : 20,
                sortBy != null ? sortBy : "name",
                direction != null ? direction : "asc"
        );
    }
}

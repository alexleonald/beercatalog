package com.beercatalogue.beer.application.dto;

import com.beercatalogue.beer.domain.model.BeerType;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

public record SearchBeerQuery(
        Integer page,
        Integer size,
        String sortBy,
        String direction,
        Optional<String> name,
        Optional<BeerType> type,
        Optional<BigDecimal> minAbv,
        Optional<BigDecimal> maxAbv,
        Optional<UUID> manufacturerId
) {
    public static SearchBeerQuery of(
            Integer page,
            Integer size,
            String sortBy,
            String direction,
            String name,
            BeerType type,
            BigDecimal minAbv,
            BigDecimal maxAbv,
            UUID manufacturerId
            ) {
        return new SearchBeerQuery(
                Optional.ofNullable(page).orElse(0),
                Optional.ofNullable(size).orElse(20),
                Optional.ofNullable(sortBy).orElse("name"),
                Optional.ofNullable(direction).orElse("asc"),
                Optional.ofNullable(name),
                Optional.ofNullable(type),
                Optional.ofNullable(minAbv),
                Optional.ofNullable(maxAbv),
                Optional.ofNullable(manufacturerId)
        );
    }
}

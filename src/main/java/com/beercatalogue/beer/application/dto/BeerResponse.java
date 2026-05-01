package com.beercatalogue.beer.application.dto;

import com.beercatalogue.beer.domain.model.BeerId;
import com.beercatalogue.beer.domain.model.BeerType;
import com.beercatalogue.manufacturer.domain.model.ManufacturerId;

import java.math.BigDecimal;

public record BeerResponse(
        BeerId id,
        String name,
        BigDecimal abv,
        BeerType type,
        String description,
        ManufacturerId manufacturerId
) {
}

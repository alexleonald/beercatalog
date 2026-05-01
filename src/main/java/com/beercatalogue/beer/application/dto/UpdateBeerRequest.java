package com.beercatalogue.beer.application.dto;

import com.beercatalogue.beer.domain.model.BeerType;
import com.beercatalogue.manufacturer.domain.model.ManufacturerId;
import jakarta.validation.constraints.NotBlank;

import java.math.BigDecimal;

public record UpdateBeerRequest(
        @NotBlank(message = "Name cannot be blank")
        String name,
        @NotBlank(message = "Abv cannot be blank")
        BigDecimal abv,
        @NotBlank(message = "Type cannot be blank")
        BeerType type,
        @NotBlank(message = "Description cannot be blank")
        String description,
        @NotBlank(message = "ManufacturerId cannot be blank")
        ManufacturerId manufacturerId
) {
}

package com.beercatalogue.beer.application.dto;

import com.beercatalogue.beer.domain.model.BeerType;
import com.beercatalogue.manufacturer.domain.model.ManufacturerId;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;

public record CreateBeerRequest(
        @NotBlank(message = "Name is required")
        String name,
        @NotNull(message = "ABV is required")
        @PositiveOrZero(message = "ABV cannot be negative")
        @DecimalMax(value = "50.0", inclusive = false, message = "ABV must be < 50")
        BigDecimal abv,
        @NotNull(message = "BeerType is required")
        BeerType type,
        @NotBlank(message = "Description is required")
        String description,
        @NotNull(message = "Manufacturer ID is required")
        ManufacturerId manufacturerId
) {
}

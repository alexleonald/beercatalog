package com.beercatalogue.manufacturer.application.dto;

import com.beercatalogue.manufacturer.domain.model.ManufacturerId;

public record ManufacturerResponse(
        ManufacturerId id,
        String name,
        String country
) {
}

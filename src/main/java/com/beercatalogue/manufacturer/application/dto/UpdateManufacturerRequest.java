package com.beercatalogue.manufacturer.application.dto;

import jakarta.validation.constraints.NotBlank;

public record UpdateManufacturerRequest(
        @NotBlank(message = "Name cannot be blank")
        String name,
        @NotBlank(message = "Country cannot be blank")
        String country
) {
}

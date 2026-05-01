package com.beercatalogue.manufacturer.application.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateManufacturerRequest(
        @NotBlank(message = "Name is required")
        String name,
        @NotBlank(message = "Country is required")
        String country
) { }

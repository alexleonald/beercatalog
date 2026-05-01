package com.beercatalogue.manufacturer.application.dto;

import java.util.List;

public record ManufacturerPageResponse(
        List<ManufacturerResponse> manufacturers,
        int page,
        int size,
        long totalElements
) {
}

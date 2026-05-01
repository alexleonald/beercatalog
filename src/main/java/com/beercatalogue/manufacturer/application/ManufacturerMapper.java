package com.beercatalogue.manufacturer.application;

import com.beercatalogue.manufacturer.application.dto.ManufacturerResponse;
import com.beercatalogue.manufacturer.domain.model.Manufacturer;

import java.util.List;

public interface ManufacturerMapper {
    ManufacturerResponse toResponse(Manufacturer manufacturer);
    List<ManufacturerResponse> toResponseList(List<Manufacturer> manufacturers);
}

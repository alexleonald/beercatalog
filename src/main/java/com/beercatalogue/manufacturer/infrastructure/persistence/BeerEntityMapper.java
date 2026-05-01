package com.beercatalogue.manufacturer.infrastructure.persistence;

import com.beercatalogue.manufacturer.domain.model.Manufacturer;

public interface BeerEntityMapper {
    ManufacturerEntity toEntity(Manufacturer manufacturer);
    Manufacturer toDomain(ManufacturerEntity entity);
}

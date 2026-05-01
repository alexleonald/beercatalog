package com.beercatalogue.manufacturer.infrastructure.persistence;

import com.beercatalogue.manufacturer.domain.model.Manufacturer;
import com.beercatalogue.manufacturer.domain.model.ManufacturerId;
import org.springframework.stereotype.Component;

@Component
public class ManufacturerEntityMapperImpl implements BeerEntityMapper {

    @Override
    public ManufacturerEntity toEntity(Manufacturer manufacturer) {
        if (manufacturer == null) {
            return null;
        }
        ManufacturerEntity entity = new ManufacturerEntity();
        if (manufacturer.getId() != null) {
            entity.setId(manufacturer.getId().value());
        }
        entity.setName(manufacturer.getName());
        entity.setCountry(manufacturer.getCountry());
        entity.setOwnerId(manufacturer.getOwnerId());
        return entity;
    }

    @Override
    public Manufacturer toDomain(ManufacturerEntity entity) {
        if (entity == null) {
            return null;
        }
        return new Manufacturer(
                ManufacturerId.of(entity.getId()),
                entity.getName(),
                entity.getCountry(),
                entity.getOwnerId()
        );
    }
}

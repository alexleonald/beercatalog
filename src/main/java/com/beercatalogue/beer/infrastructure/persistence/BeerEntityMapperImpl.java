package com.beercatalogue.beer.infrastructure.persistence;

import com.beercatalogue.beer.domain.model.Beer;
import com.beercatalogue.beer.domain.model.BeerId;
import com.beercatalogue.beer.domain.model.ImageFile;
import com.beercatalogue.manufacturer.domain.model.ManufacturerId;
import org.springframework.stereotype.Component;

@Component
public class BeerEntityMapperImpl implements BeerEntityMapper {

    @Override
    public BeerEntity toEntity(Beer beer) {
        if (beer == null) {
            return null;
        }
        return new BeerEntity(
                beer.getId().value(),
                beer.getName(),
                beer.getAbv(),
                beer.getType(),
                beer.getDescription(),
                beer.getManufacturerId() != null ? beer.getManufacturerId().value() : null,
                beer.getImageFile() != null ? beer.getImageFile().imageUrl() : null
        );
    }

    @Override
    public Beer toDomain(BeerEntity entity) {
        if (entity == null) {
            return null;
        }
        return new Beer(
                BeerId.of(entity.getId()),
                entity.getName(),
                entity.getAbv(),
                entity.getType(),
                entity.getDescription(),
                entity.getManufacturerId() != null ? ManufacturerId.of(entity.getManufacturerId()) : null,
                entity.getImageUrl() != null ? ImageFile.of(entity.getImageUrl()) : null
        );
    }
}

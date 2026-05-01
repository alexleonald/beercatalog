package com.beercatalogue.beer.infrastructure.persistence;

import com.beercatalogue.beer.domain.model.Beer;

public interface BeerEntityMapper {
    BeerEntity toEntity(Beer beer);
    Beer toDomain(BeerEntity entity);

}

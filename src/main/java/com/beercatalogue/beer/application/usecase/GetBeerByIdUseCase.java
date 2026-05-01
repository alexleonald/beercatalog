package com.beercatalogue.beer.application.usecase;

import com.beercatalogue.beer.application.dto.BeerResponse;
import com.beercatalogue.beer.domain.model.BeerId;

public interface GetBeerByIdUseCase {
    BeerResponse findBeerById(BeerId beerId);
}
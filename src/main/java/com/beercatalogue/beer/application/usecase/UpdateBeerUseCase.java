package com.beercatalogue.beer.application.usecase;

import com.beercatalogue.beer.application.dto.BeerResponse;
import com.beercatalogue.beer.application.dto.UpdateBeerRequest;
import com.beercatalogue.beer.domain.model.BeerId;

public interface UpdateBeerUseCase {
    BeerResponse updateBeer(BeerId id, UpdateBeerRequest updateBeerRequest);
}

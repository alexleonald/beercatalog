package com.beercatalogue.beer.application.usecase;

import com.beercatalogue.beer.application.dto.BeerResponse;
import com.beercatalogue.beer.application.dto.CreateBeerRequest;

public interface CreateBeerUseCase {
    BeerResponse createBeer(CreateBeerRequest createBeerRequest);
}

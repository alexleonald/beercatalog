package com.beercatalogue.beer.application.usecase;

import com.beercatalogue.beer.application.dto.BeerPageResponse;
import com.beercatalogue.beer.application.dto.GetAllBeerQuery;

public interface GetAllBeerUseCase {
    BeerPageResponse findAllBeers(GetAllBeerQuery getAllBeerQuery);
}
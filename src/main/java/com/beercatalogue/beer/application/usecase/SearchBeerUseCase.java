package com.beercatalogue.beer.application.usecase;

import com.beercatalogue.beer.application.dto.BeerPageResponse;
import com.beercatalogue.beer.application.dto.SearchBeerQuery;

public interface SearchBeerUseCase {
    BeerPageResponse searchBeers(SearchBeerQuery query);
}

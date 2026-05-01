package com.beercatalogue.beer.application.port;

import com.beercatalogue.beer.application.dto.SearchBeerQuery;
import com.beercatalogue.beer.domain.model.Beer;
import com.beercatalogue.beer.domain.model.BeerId;
import com.beercatalogue.common.domain.model.PageRequest;
import com.beercatalogue.common.domain.model.PageResult;

import java.util.Optional;

public interface BeerRepositoryPort {
    Beer save(Beer beer);
    Optional<Beer> findById(BeerId id);
    PageResult<Beer> findAll(PageRequest pageRequest);
    void deleteById(BeerId id);
    Beer update(Beer beer);
    PageResult<Beer> search(SearchBeerQuery query);
}
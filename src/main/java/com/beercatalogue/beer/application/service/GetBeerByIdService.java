package com.beercatalogue.beer.application.service;

import com.beercatalogue.beer.application.dto.BeerResponse;
import com.beercatalogue.beer.application.mapper.BeerMapper;
import com.beercatalogue.beer.application.usecase.GetBeerByIdUseCase;
import com.beercatalogue.beer.domain.model.Beer;
import com.beercatalogue.beer.domain.model.BeerId;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class GetBeerByIdService implements GetBeerByIdUseCase {
    private final BeerMapper beerMapper;
    private final BeerFinder beerFinder;

    public GetBeerByIdService(BeerMapper beerMapper, BeerFinder beerFinder) {
        this.beerMapper = beerMapper;
        this.beerFinder = beerFinder;
    }

    @Override
    public BeerResponse findBeerById(BeerId beerId) {
        log.info("Attempting to find beer by ID: {}", beerId.value());

        Beer beer = beerFinder.getBeerByIdOrThrow(beerId);
        log.info("Beer with ID {} found.", beerId.value());
        return beerMapper.toResponse(beer);
    }
}

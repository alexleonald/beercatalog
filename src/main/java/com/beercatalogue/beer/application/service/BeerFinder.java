package com.beercatalogue.beer.application.service;

import com.beercatalogue.beer.application.port.BeerRepositoryPort;
import com.beercatalogue.beer.domain.exception.BeerNotFoundException;
import com.beercatalogue.beer.domain.model.Beer;
import com.beercatalogue.beer.domain.model.BeerId;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class BeerFinder {

    private final BeerRepositoryPort beerRepository;

    public BeerFinder(BeerRepositoryPort beerRepository) {
        this.beerRepository = beerRepository;
    }

    public Beer getBeerByIdOrThrow(BeerId id) {
        return beerRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Beer with ID {} not found.", id.value());
                    return new BeerNotFoundException("Beer not found: " + id);
                });
    }

}

package com.beercatalogue.beer.application.service;

import com.beercatalogue.beer.application.dto.BeerImageResponse;
import com.beercatalogue.beer.application.usecase.RetrieveBeerImageUseCase;
import com.beercatalogue.beer.domain.model.Beer;
import com.beercatalogue.beer.domain.model.BeerId;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class RetrieveBeerImageService implements RetrieveBeerImageUseCase {
    private final BeerFinder beerFinder;

    public RetrieveBeerImageService(BeerFinder beerFinder) {
        this.beerFinder = beerFinder;
    }

    @Override
    public BeerImageResponse retrieveBeerImage(BeerId beerId) {
        log.info("Attempting to retrieve image for beer with ID: {}", beerId.value());
        Beer beer = beerFinder.getBeerByIdOrThrow(beerId);

        if (beer.getImageFile() == null) {
            return new BeerImageResponse(null);
        }

        log.info("Image for beer {} retrieved successfully. URL: {}", beerId.value(), beer.getImageFile().imageUrl());
        return new BeerImageResponse(beer.getImageFile().imageUrl());
    }

}

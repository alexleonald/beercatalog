package com.beercatalogue.beer.application.service;

import com.beercatalogue.beer.application.dto.BeerImageResponse;
import com.beercatalogue.beer.application.mapper.BeerMapper;
import com.beercatalogue.beer.application.port.BeerRepositoryPort;
import com.beercatalogue.beer.application.usecase.AttachImageToBeerUseCase;
import com.beercatalogue.beer.domain.model.Beer;
import com.beercatalogue.beer.domain.model.BeerId;
import com.beercatalogue.beer.domain.model.ImageFile;
import com.beercatalogue.common.application.SecurityPort;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AttachImageToBeerService implements AttachImageToBeerUseCase {

    private final BeerRepositoryPort beerRepository;
    private final BeerFinder beerFinder;
    private final BeerMapper beerMapper;

    public AttachImageToBeerService(BeerRepositoryPort beerRepository, BeerFinder beerFinder, BeerMapper beerMapper, SecurityPort securityPort) {
        this.beerRepository = beerRepository;
        this.beerFinder = beerFinder;
        this.beerMapper = beerMapper;
    }

    @Override
    public BeerImageResponse attachImageToBeer(BeerId beerId, ImageFile imageFile) {
        log.info("Attempting to attach image to beer with ID: {}", beerId.value());
        log.debug("Image URL: {}", imageFile.imageUrl());
        Beer beer = beerFinder.getBeerByIdOrThrow(beerId);

        beer.attachImage(imageFile);
        Beer saved = beerRepository.save(beer);
        log.info("Image attached successfully to beer {}. Image URL: {}", beerId.value(), imageFile.imageUrl());
        return beerMapper.toImageResponse(saved);
    }
}

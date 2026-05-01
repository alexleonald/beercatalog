package com.beercatalogue.beer.application.service;

import com.beercatalogue.beer.application.dto.BeerResponse;
import com.beercatalogue.beer.application.dto.CreateBeerRequest;
import com.beercatalogue.beer.application.mapper.BeerMapper;
import com.beercatalogue.beer.application.port.BeerRepositoryPort;
import com.beercatalogue.beer.application.port.ManufacturerClientPort;
import com.beercatalogue.beer.application.usecase.CreateBeerUseCase;
import com.beercatalogue.beer.domain.model.Beer;
import com.beercatalogue.beer.domain.model.BeerId;
import com.beercatalogue.manufacturer.domain.model.Manufacturer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class CreateBeerService implements CreateBeerUseCase {

    private final BeerRepositoryPort repositoryPort;
    private final ManufacturerClientPort manufacturerClientPort;
    private final BeerMapper beerMapper;

    public CreateBeerService(BeerRepositoryPort repositoryPort, ManufacturerClientPort manufacturerClientPort, BeerMapper beerMapper) {
        this.repositoryPort = repositoryPort;
        this.manufacturerClientPort = manufacturerClientPort;
        this.beerMapper = beerMapper;
    }

    @Override
    public BeerResponse createBeer(CreateBeerRequest createBeerRequest) {
        log.info("Attempting to create beer: {}", createBeerRequest.name());
        log.debug("Beer creation request details: {}", createBeerRequest);

        Manufacturer manufacturer = manufacturerClientPort.getByIdOrThrow(createBeerRequest.manufacturerId());
        log.debug("Manufacturer {} found for beer creation.", manufacturer.getId().value());

        Beer beer = new Beer(
                BeerId.generate(),
                createBeerRequest.name(),
                createBeerRequest.abv(),
                createBeerRequest.type(),
                createBeerRequest.description(),
                manufacturer.getId()
        );
        Beer savedBeer = repositoryPort.save(beer);
        log.info("Beer '{}' created successfully with ID: {}", savedBeer.getName(), savedBeer.getId().value());
        return beerMapper.toResponse(savedBeer);
    }

}

package com.beercatalogue.beer.application.service;

import com.beercatalogue.beer.application.dto.BeerResponse;
import com.beercatalogue.beer.application.dto.UpdateBeerRequest;
import com.beercatalogue.beer.application.mapper.BeerMapper;
import com.beercatalogue.beer.application.port.BeerRepositoryPort;
import com.beercatalogue.beer.application.port.ManufacturerClientPort;
import com.beercatalogue.beer.application.usecase.UpdateBeerUseCase;
import com.beercatalogue.beer.domain.model.Beer;
import com.beercatalogue.beer.domain.model.BeerId;
import com.beercatalogue.common.application.SecurityPort;
import com.beercatalogue.common.domain.exception.UnauthorizedActionException;
import com.beercatalogue.manufacturer.domain.model.ManufacturerId;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static com.beercatalogue.common.infrastructure.ApiConstants.ROLE_ADMIN;

@Slf4j
@Service
public class UpdateBeerService implements UpdateBeerUseCase {

    private final BeerFinder beerFinder;
    private final BeerMapper beerMapper;
    private final BeerRepositoryPort beerRepository;
    private final ManufacturerClientPort manufacturerClientPort;
    private final SecurityPort securityPort;

    public UpdateBeerService(BeerFinder beerFinder, BeerMapper beerMapper, BeerRepositoryPort beerRepository, ManufacturerClientPort manufacturerClientPort, SecurityPort securityPort) {
        this.beerFinder = beerFinder;
        this.beerMapper = beerMapper;
        this.beerRepository = beerRepository;
        this.manufacturerClientPort = manufacturerClientPort;
        this.securityPort = securityPort;
    }

    /**
     * Updates a beer.
     * <p>
     * Rules:
     * - ADMIN can update any beer
     * - In case we update the manufacturer, it should exist
     */
    @Override
    public BeerResponse updateBeer(BeerId beerId, UpdateBeerRequest request) {
        log.info("Attempting to update beer with ID: {}", beerId.value());
        log.debug("Update request: {}", request);

        Beer existing = beerFinder.getBeerByIdOrThrow(beerId);
        ManufacturerId newManufacturerId = request.manufacturerId();

        if (!securityPort.hasRole(ROLE_ADMIN)) {
            log.warn("User {} is not authorized to update beer {}. Only ADMIN can update beers.", securityPort.getCurrentUserId(), beerId.value());
            throw new UnauthorizedActionException("User " + securityPort.getCurrentUserId() + " is not authorized to update beer " + beerId.value() + ". ");
        }

        if (!newManufacturerId.equals(existing.getManufacturerId())){
            log.debug("Manufacturer ID is being changed from {} to {}. Verifying new manufacturer.", existing.getManufacturerId().value(), newManufacturerId.value());
            manufacturerClientPort.getByIdOrThrow(newManufacturerId);
            log.debug("New manufacturer {} verified.", newManufacturerId.value());
            manufacturerClientPort.getByIdOrThrow(newManufacturerId);
        }

        existing.updateBeerDetails(
                request.name(),
                request.abv(),
                request.type(),
                request.description(),
                newManufacturerId
        );
        Beer updatedBeer = beerRepository.save(existing);
        log.info("Beer with ID {} updated successfully.", beerId.value());
        return beerMapper.toResponse(updatedBeer);
    }
}

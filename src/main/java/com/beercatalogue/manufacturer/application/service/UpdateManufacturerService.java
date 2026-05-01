package com.beercatalogue.manufacturer.application.service;

import com.beercatalogue.common.application.SecurityPort;
import com.beercatalogue.common.domain.exception.UnauthorizedActionException;
import com.beercatalogue.manufacturer.application.ManufacturerMapper;
import com.beercatalogue.manufacturer.application.dto.ManufacturerResponse;
import com.beercatalogue.manufacturer.application.dto.UpdateManufacturerRequest;
import com.beercatalogue.manufacturer.application.port.ManufacturerRepositoryPort;
import com.beercatalogue.manufacturer.application.usecase.UpdateManufacturerUseCase;
import com.beercatalogue.manufacturer.domain.model.Manufacturer;
import com.beercatalogue.manufacturer.domain.model.ManufacturerId;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static com.beercatalogue.common.infrastructure.ApiConstants.ROLE_ADMIN;

@Slf4j
@Service
public class UpdateManufacturerService implements UpdateManufacturerUseCase {
    private final ManufacturerRepositoryPort manufacturerRepositoryPort;
    private final ManufacturerFinder manufacturerFinder;
    private final ManufacturerMapper manufacturerMapper;
    private final SecurityPort securityPort;


    public UpdateManufacturerService(ManufacturerRepositoryPort manufacturerRepositoryPort, ManufacturerFinder manufacturerFinder, ManufacturerMapper manufacturerMapper, SecurityPort securityPort) {
        this.manufacturerRepositoryPort = manufacturerRepositoryPort;
        this.manufacturerFinder = manufacturerFinder;
        this.manufacturerMapper = manufacturerMapper;
        this.securityPort = securityPort;
    }

    /**
     * Updates a manufacturer.
     * <p>
     * Rules: <p>
     * - ADMIN can update any manufacturer
     * - MANUFACTURER can only update their own
     */
    @Override
    public ManufacturerResponse updateManufacturer(ManufacturerId id, UpdateManufacturerRequest request) {
        log.info("Attempting to update manufacturer with ID: {}", id.value());
        log.debug("Update request: {}", request);

        Manufacturer existing = manufacturerFinder.getManufacturerByIdOrThrow(id);

        String currentUserId = securityPort.getCurrentUserId();
        if (currentUserId == null) {
            log.warn("User is not authenticated. Attempting to update manufacturer {}.", id.value());
            throw new UnauthorizedActionException("User not authenticated.");
        }
        if (!securityPort.hasRole(ROLE_ADMIN) && !existing.getOwnerId().equals(currentUserId)) {
            log.warn("User {} is not authorized to update manufacturer {}. Owner: {}", currentUserId, id.value(), existing.getOwnerId());
            throw new UnauthorizedActionException("User " + currentUserId + " is not authorized to update manufacturer " + id.value());
        }

        Manufacturer updated = new Manufacturer(
                id,
                request.name(),
                request.country(),
                existing.getOwnerId()
        );
        Manufacturer saved = manufacturerRepositoryPort.update(updated);
        log.info("Manufacturer with ID {} updated successfully.", id.value());
        return manufacturerMapper.toResponse(saved);
    }
}

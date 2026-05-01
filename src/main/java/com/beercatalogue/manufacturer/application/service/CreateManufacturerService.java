package com.beercatalogue.manufacturer.application.service;

import com.beercatalogue.common.application.SecurityPort;
import com.beercatalogue.common.domain.exception.UnauthorizedActionException;
import com.beercatalogue.manufacturer.application.ManufacturerMapper;
import com.beercatalogue.manufacturer.application.dto.CreateManufacturerRequest;
import com.beercatalogue.manufacturer.application.dto.ManufacturerResponse;
import com.beercatalogue.manufacturer.application.port.ManufacturerRepositoryPort;
import com.beercatalogue.manufacturer.application.usecase.CreateManufacturerUseCase;
import com.beercatalogue.manufacturer.domain.model.Manufacturer;
import com.beercatalogue.manufacturer.domain.model.ManufacturerId;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class CreateManufacturerService implements CreateManufacturerUseCase {

    private final ManufacturerRepositoryPort manufacturerRepositoryPort;
    private final ManufacturerMapper manufacturerMapper;
    private final SecurityPort securityPort;

    public CreateManufacturerService(ManufacturerRepositoryPort manufacturerRepositoryPort, ManufacturerMapper manufacturerMapper, SecurityPort securityPort){
        this.manufacturerRepositoryPort = manufacturerRepositoryPort;
        this.manufacturerMapper = manufacturerMapper;
        this.securityPort = securityPort;
    }

    @Override
    public ManufacturerResponse createManufacturer(CreateManufacturerRequest createManufacturerRequest) {
        log.info("Attempting to create manufacturer with name: {}", createManufacturerRequest.name());

        String currentUserId = securityPort.getCurrentUserId();

        if (currentUserId == null) {
            log.warn("Unauthorized attempt to create manufacturer: User not authenticated.");
            throw new UnauthorizedActionException("User must be authenticated to create a manufacturer");
        }

        Manufacturer manufacturerToSave = new Manufacturer(
                ManufacturerId.generate(),
                createManufacturerRequest.name(),
                createManufacturerRequest.country(),
                currentUserId
        );
        Manufacturer manufacturer = manufacturerRepositoryPort.save(manufacturerToSave);
        log.info("Manufacturer created successfully with ID: {}", manufacturer.getId().value());
        return manufacturerMapper.toResponse(manufacturer);
    }
}

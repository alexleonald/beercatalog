package com.beercatalogue.manufacturer.application.service;

import com.beercatalogue.manufacturer.application.port.ManufacturerRepositoryPort;
import com.beercatalogue.manufacturer.domain.exception.ManufacturerNotFoundException;
import com.beercatalogue.manufacturer.domain.model.Manufacturer;
import com.beercatalogue.manufacturer.domain.model.ManufacturerId;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ManufacturerFinder {

    private final ManufacturerRepositoryPort repositoryPort;

    public ManufacturerFinder(ManufacturerRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    public Manufacturer getManufacturerByIdOrThrow(ManufacturerId id) {
        return repositoryPort.findById(id)
                .orElseThrow(() -> {
                    log.warn("Manufacturer with ID {} not found.", id.value());
                    return new ManufacturerNotFoundException("Manufacturer not found: " + id);
                });
    }
}
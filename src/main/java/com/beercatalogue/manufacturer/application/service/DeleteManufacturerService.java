package com.beercatalogue.manufacturer.application.service;

import com.beercatalogue.manufacturer.application.port.ManufacturerRepositoryPort;
import com.beercatalogue.manufacturer.application.usecase.DeleteManufacturerUseCase;
import com.beercatalogue.manufacturer.domain.model.Manufacturer;
import com.beercatalogue.manufacturer.domain.model.ManufacturerId;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class DeleteManufacturerService implements DeleteManufacturerUseCase {
    private final ManufacturerRepositoryPort manufacturerRepositoryPort;
    private final ManufacturerFinder manufacturerFinder;

    public DeleteManufacturerService(ManufacturerRepositoryPort manufacturerRepositoryPort, ManufacturerFinder manufacturerFinder) {
        this.manufacturerRepositoryPort = manufacturerRepositoryPort;
        this.manufacturerFinder = manufacturerFinder;
    }

    /**
     * Delete a manufacturer.
     * <p>
     * All users can do it, in case business wants a different behaviour, we can adjust it.
     */
    @Override
    @Transactional
    public void deleteManufacturer(ManufacturerId id) {
        log.info("Attempting to delete manufacturer with ID: {}", id.value());
        Manufacturer manufacturer = manufacturerFinder.getManufacturerByIdOrThrow(id);
        manufacturerRepositoryPort.deleteById(manufacturer.getId());
        log.info("Manufacturer with ID {} deleted successfully.", id.value());
    }
}

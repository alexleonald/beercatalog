package com.beercatalogue.manufacturer.infrastructure;

import com.beercatalogue.beer.application.port.ManufacturerClientPort;
import com.beercatalogue.manufacturer.application.port.ManufacturerRepositoryPort;
import com.beercatalogue.manufacturer.domain.exception.ManufacturerNotFoundException;
import com.beercatalogue.manufacturer.domain.model.Manufacturer;
import com.beercatalogue.manufacturer.domain.model.ManufacturerId;
import org.springframework.stereotype.Component;

@Component
public class ManufacturerClientAdapter implements ManufacturerClientPort {

    private final ManufacturerRepositoryPort repositoryPort;

    public ManufacturerClientAdapter(ManufacturerRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public Manufacturer getByIdOrThrow(ManufacturerId id) {
        return repositoryPort.findById(id)
                .orElseThrow(() -> new ManufacturerNotFoundException("Manufacturer not found: " + id));
    }
}

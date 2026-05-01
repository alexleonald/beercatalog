package com.beercatalogue.manufacturer.application.port;

import com.beercatalogue.common.domain.model.PageRequest;
import com.beercatalogue.common.domain.model.PageResult;
import com.beercatalogue.manufacturer.domain.model.Manufacturer;
import com.beercatalogue.manufacturer.domain.model.ManufacturerId;

import java.util.Optional;

public interface ManufacturerRepositoryPort {
    Manufacturer save(Manufacturer manufacturer);
    Optional<Manufacturer> findById(ManufacturerId id);
    PageResult<Manufacturer> findAll(PageRequest pageRequest);
    void deleteById(ManufacturerId id);
    Manufacturer update(Manufacturer manufacturer);
}

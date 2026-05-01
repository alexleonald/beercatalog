package com.beercatalogue.manufacturer.application.usecase;

import com.beercatalogue.manufacturer.domain.model.ManufacturerId;

public interface DeleteManufacturerUseCase {
    void deleteManufacturer(ManufacturerId id);
}

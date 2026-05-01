package com.beercatalogue.manufacturer.application.usecase;

import com.beercatalogue.manufacturer.application.dto.ManufacturerResponse;
import com.beercatalogue.manufacturer.application.dto.UpdateManufacturerRequest;
import com.beercatalogue.manufacturer.domain.model.ManufacturerId;

public interface UpdateManufacturerUseCase {
    ManufacturerResponse updateManufacturer(ManufacturerId id, UpdateManufacturerRequest updateManufacturerRequest);
}

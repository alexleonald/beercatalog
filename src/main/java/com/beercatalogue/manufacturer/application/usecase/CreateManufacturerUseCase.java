package com.beercatalogue.manufacturer.application.usecase;

import com.beercatalogue.manufacturer.application.dto.CreateManufacturerRequest;
import com.beercatalogue.manufacturer.application.dto.ManufacturerResponse;

public interface CreateManufacturerUseCase {
    ManufacturerResponse createManufacturer(CreateManufacturerRequest createManufacturerRequest);
}

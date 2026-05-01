package com.beercatalogue.manufacturer.application.usecase;

import com.beercatalogue.manufacturer.application.dto.ManufacturerResponse;
import com.beercatalogue.manufacturer.domain.model.ManufacturerId;

public interface GetManufacturerByIdUseCase {
    ManufacturerResponse findManufacturerById(ManufacturerId id);
}
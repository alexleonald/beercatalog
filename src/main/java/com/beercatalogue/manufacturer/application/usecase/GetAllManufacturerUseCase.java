package com.beercatalogue.manufacturer.application.usecase;

import com.beercatalogue.manufacturer.application.dto.GetAllManufacturerQuery;
import com.beercatalogue.manufacturer.application.dto.ManufacturerPageResponse;

public interface GetAllManufacturerUseCase {
    ManufacturerPageResponse findAllManufacturers(GetAllManufacturerQuery getAllManufacturerQuery);
}
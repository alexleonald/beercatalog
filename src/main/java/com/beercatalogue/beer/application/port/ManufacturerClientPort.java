package com.beercatalogue.beer.application.port;

import com.beercatalogue.manufacturer.domain.model.Manufacturer;
import com.beercatalogue.manufacturer.domain.model.ManufacturerId;

public interface ManufacturerClientPort {
    Manufacturer getByIdOrThrow(ManufacturerId id);
}

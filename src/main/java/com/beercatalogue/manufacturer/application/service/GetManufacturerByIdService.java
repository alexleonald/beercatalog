package com.beercatalogue.manufacturer.application.service;

import com.beercatalogue.manufacturer.application.ManufacturerMapper;
import com.beercatalogue.manufacturer.application.dto.ManufacturerResponse;
import com.beercatalogue.manufacturer.application.usecase.GetManufacturerByIdUseCase;
import com.beercatalogue.manufacturer.domain.model.Manufacturer;
import com.beercatalogue.manufacturer.domain.model.ManufacturerId;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class GetManufacturerByIdService implements GetManufacturerByIdUseCase{
    private final ManufacturerMapper manufacturerMapper;
    private final ManufacturerFinder manufacturerFinder;


    public GetManufacturerByIdService(ManufacturerMapper manufacturerMapper, ManufacturerFinder manufacturerFinder) {
        this.manufacturerMapper = manufacturerMapper;
        this.manufacturerFinder = manufacturerFinder;
    }

    @Override
    public ManufacturerResponse findManufacturerById(ManufacturerId manufacturerId) {
        log.info("Attempting to find manufacturer by ID: {}", manufacturerId.value());
        Manufacturer manufacturer = manufacturerFinder.getManufacturerByIdOrThrow(manufacturerId);
        log.info("Manufacturer with ID {} found.", manufacturerId.value());
        return manufacturerMapper.toResponse(manufacturer);
    }
}

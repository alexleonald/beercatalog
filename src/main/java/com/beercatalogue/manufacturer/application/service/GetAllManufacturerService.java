package com.beercatalogue.manufacturer.application.service;

import com.beercatalogue.common.domain.model.PageRequest;
import com.beercatalogue.common.domain.model.PageResult;
import com.beercatalogue.manufacturer.application.ManufacturerMapper;
import com.beercatalogue.manufacturer.application.dto.GetAllManufacturerQuery;
import com.beercatalogue.manufacturer.application.dto.ManufacturerPageResponse;
import com.beercatalogue.manufacturer.application.dto.ManufacturerResponse;
import com.beercatalogue.manufacturer.application.port.ManufacturerRepositoryPort;
import com.beercatalogue.manufacturer.application.usecase.GetAllManufacturerUseCase;
import com.beercatalogue.manufacturer.domain.model.Manufacturer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class GetAllManufacturerService implements GetAllManufacturerUseCase {

    private final ManufacturerRepositoryPort manufacturerRepositoryPort;
    private final ManufacturerMapper manufacturerMapper;

    public GetAllManufacturerService(ManufacturerRepositoryPort manufacturerRepositoryPort, ManufacturerMapper manufacturerMapper) {
        this.manufacturerRepositoryPort = manufacturerRepositoryPort;
        this.manufacturerMapper = manufacturerMapper;
    }

    @Override
    public ManufacturerPageResponse findAllManufacturers(GetAllManufacturerQuery query) {
        log.info("Fetching all manufacturers with page: {}, size: {}, sortBy: {}, direction: {}",
                query.page(), query.size(), query.sortBy(), query.direction());

        PageRequest pageRequest = new PageRequest(query.page(), query.size(), query.sortBy(), query.direction());
        PageResult<Manufacturer> manufacturers = manufacturerRepositoryPort.findAll(pageRequest);
        List<ManufacturerResponse> manufacturerResponses = manufacturerMapper.toResponseList(manufacturers.content());
        log.info("Found {} manufacturers on page {} of {} elements.", manufacturerResponses.size(), manufacturers.page(), manufacturers.totalElements());
        return new ManufacturerPageResponse(
                manufacturerResponses,
                manufacturers.page(),
                manufacturers.size(),
                manufacturers.totalElements());
    }
}

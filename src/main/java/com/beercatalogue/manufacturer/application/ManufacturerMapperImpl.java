package com.beercatalogue.manufacturer.application;

import com.beercatalogue.manufacturer.application.dto.ManufacturerResponse;
import com.beercatalogue.manufacturer.domain.model.Manufacturer;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ManufacturerMapperImpl implements ManufacturerMapper {

    @Override
    public ManufacturerResponse toResponse(Manufacturer manufacturer) {
        if (manufacturer == null) {
            return null;
        }
        return new ManufacturerResponse(
                manufacturer.getId(),
                manufacturer.getName(),
                manufacturer.getCountry()
        );
    }

    @Override
    public List<ManufacturerResponse> toResponseList(List<Manufacturer> manufacturers) {
        if (manufacturers == null) {
            return null;
        }
        return manufacturers.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }
}

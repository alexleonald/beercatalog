package com.beercatalogue.beer.application.mapper;

import com.beercatalogue.beer.application.dto.BeerImageResponse;
import com.beercatalogue.beer.application.dto.BeerResponse;
import com.beercatalogue.beer.domain.model.Beer;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class BeerMapperImpl implements BeerMapper {

    @Override
    public BeerResponse toResponse(Beer beer) {
        if (beer == null) {
            return null;
        }
        return new BeerResponse(
                beer.getId(),
                beer.getName(),
                beer.getAbv(),
                beer.getType(),
                beer.getDescription(),
                beer.getManufacturerId()
        );
    }

    @Override
    public List<BeerResponse> toResponseList(List<Beer> content) {
        if (content == null) {
            return null;
        }
        return content.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public BeerImageResponse toImageResponse(Beer beer) {
        if (beer == null) {
            return null;
        }
        return new BeerImageResponse(beer.getImageFile().imageUrl());
    }
}

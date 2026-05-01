package com.beercatalogue.beer.application.mapper;

import com.beercatalogue.beer.application.dto.BeerImageResponse;
import com.beercatalogue.beer.application.dto.BeerResponse;
import com.beercatalogue.beer.domain.model.Beer;

import java.util.List;


public interface BeerMapper {
    BeerResponse toResponse(Beer beer) ;
    List<BeerResponse> toResponseList(List<Beer> content);
    BeerImageResponse toImageResponse(Beer beer);
}

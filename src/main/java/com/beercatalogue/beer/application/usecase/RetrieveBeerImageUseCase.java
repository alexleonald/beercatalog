package com.beercatalogue.beer.application.usecase;

import com.beercatalogue.beer.application.dto.BeerImageResponse;
import com.beercatalogue.beer.domain.model.BeerId;

public interface RetrieveBeerImageUseCase {
    BeerImageResponse retrieveBeerImage(BeerId beerId);
}

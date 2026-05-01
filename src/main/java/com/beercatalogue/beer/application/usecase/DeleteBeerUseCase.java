package com.beercatalogue.beer.application.usecase;

import com.beercatalogue.beer.domain.model.BeerId;

public interface DeleteBeerUseCase {
    void deleteBeer(BeerId id);
}

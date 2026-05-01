package com.beercatalogue.beer.application.usecase;

import com.beercatalogue.beer.application.dto.BeerImageResponse;
import com.beercatalogue.beer.domain.model.BeerId;
import com.beercatalogue.beer.domain.model.ImageFile;

public interface AttachImageToBeerUseCase {
    BeerImageResponse attachImageToBeer(BeerId beerId, ImageFile imageFile);
}

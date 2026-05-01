package com.beercatalogue.beer.application.dto;

import java.util.List;

public record BeerPageResponse(
        List<BeerResponse> beers,
        int page,
        int size,
        long totalElements
) {
}

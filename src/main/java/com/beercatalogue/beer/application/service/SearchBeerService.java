package com.beercatalogue.beer.application.service;

import com.beercatalogue.beer.application.dto.BeerPageResponse;
import com.beercatalogue.beer.application.dto.BeerResponse;
import com.beercatalogue.beer.application.dto.SearchBeerQuery;
import com.beercatalogue.beer.application.mapper.BeerMapper;
import com.beercatalogue.beer.application.port.BeerRepositoryPort;
import com.beercatalogue.beer.application.usecase.SearchBeerUseCase;
import com.beercatalogue.beer.domain.model.Beer;
import com.beercatalogue.common.domain.model.PageResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class SearchBeerService implements SearchBeerUseCase {

    private final BeerRepositoryPort beerRepositoryPort;
    private final BeerMapper beerMapper;

    public SearchBeerService(BeerRepositoryPort beerRepositoryPort, BeerMapper beerMapper) {
        this.beerRepositoryPort = beerRepositoryPort;
        this.beerMapper = beerMapper;
    }

    @Override
    public BeerPageResponse searchBeers(SearchBeerQuery query) {
        log.info("Searching beers with query: {}", query);

        PageResult<Beer> beers = beerRepositoryPort.search(query);
        List<BeerResponse> beerResponses = beerMapper.toResponseList(beers.content());
        log.info("Found {} beers matching search criteria on page {} of {}.", beerResponses.size(), beers.page(), beers.totalElements());
        return new BeerPageResponse(
                beerResponses,
                beers.page(),
                beers.size(),
                beers.totalElements());
    }
}

package com.beercatalogue.beer.application.service;

import com.beercatalogue.beer.application.dto.BeerPageResponse;
import com.beercatalogue.beer.application.dto.BeerResponse;
import com.beercatalogue.beer.application.dto.GetAllBeerQuery;
import com.beercatalogue.beer.application.mapper.BeerMapper;
import com.beercatalogue.beer.application.port.BeerRepositoryPort;
import com.beercatalogue.beer.application.usecase.GetAllBeerUseCase;
import com.beercatalogue.beer.domain.model.Beer;
import com.beercatalogue.common.domain.model.PageRequest;
import com.beercatalogue.common.domain.model.PageResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class GetAllBeerService implements GetAllBeerUseCase {

    private final BeerRepositoryPort beerRepositoryPort;
    private final BeerMapper beerMapper;

    public GetAllBeerService(BeerRepositoryPort beerRepositoryPort, BeerMapper beerMapper) {
        this.beerRepositoryPort = beerRepositoryPort;
        this.beerMapper = beerMapper;
    }

    @Override
    public BeerPageResponse findAllBeers(GetAllBeerQuery query) {
        log.info("Fetching all beers with page: {}, size: {}, sortBy: {}, direction: {}",
                query.page(), query.size(), query.sortBy(), query.direction());

        PageRequest pageRequest = new PageRequest(query.page(), query.size(), query.sortBy(), query.direction());
        PageResult<Beer> beers = beerRepositoryPort.findAll(pageRequest);
        List<BeerResponse> beerResponses = beerMapper.toResponseList(beers.content());
        log.info("Found {} beers on page {} of {} elements.", beerResponses.size(), beers.page(), beers.totalElements());
        return new BeerPageResponse(
                beerResponses,
                beers.page(),
                beers.size(),
                beers.totalElements());
    }
}

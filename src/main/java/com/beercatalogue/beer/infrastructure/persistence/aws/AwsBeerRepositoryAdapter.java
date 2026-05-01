package com.beercatalogue.beer.infrastructure.persistence.aws;

import com.beercatalogue.beer.application.dto.SearchBeerQuery;
import com.beercatalogue.beer.application.port.BeerRepositoryPort;
import com.beercatalogue.beer.domain.model.Beer;
import com.beercatalogue.beer.domain.model.BeerId;
import com.beercatalogue.beer.infrastructure.persistence.SpringDataBeerRepository;
import com.beercatalogue.common.domain.model.PageRequest;
import com.beercatalogue.common.domain.model.PageResult;
import com.beercatalogue.manufacturer.infrastructure.persistence.BeerEntityMapper;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Profile("aws")
@Component
public class AwsBeerRepositoryAdapter implements BeerRepositoryPort {

    private final SpringDataBeerRepository jpaRepository;
    private final BeerEntityMapper beerEntityMapper;

    public AwsBeerRepositoryAdapter(SpringDataBeerRepository jpaRepository, BeerEntityMapper beerEntityMapper) {
        this.jpaRepository = jpaRepository;
        this.beerEntityMapper = beerEntityMapper;
    }

    @Override
    public Beer save(Beer beer) {
        return null;
    }

    @Override
    public Optional<Beer> findById(BeerId id) {
        return Optional.empty();
    }

    @Override
    public PageResult<Beer> findAll(PageRequest pageRequest) {
        return null;
    }

    @Override
    public void deleteById(BeerId id) {

    }

    @Override
    public Beer update(Beer beer) {
        return null;
    }

    @Override
    public PageResult<Beer> search(SearchBeerQuery query) {
        return null;
    }

}

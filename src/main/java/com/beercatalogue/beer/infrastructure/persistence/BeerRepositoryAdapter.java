package com.beercatalogue.beer.infrastructure.persistence;

import com.beercatalogue.beer.application.dto.SearchBeerQuery;
import com.beercatalogue.beer.application.port.BeerRepositoryPort;
import com.beercatalogue.beer.domain.model.Beer;
import com.beercatalogue.beer.domain.model.BeerId;
import com.beercatalogue.common.domain.model.PageRequest;
import com.beercatalogue.common.domain.model.PageResult;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class BeerRepositoryAdapter implements BeerRepositoryPort {

    private final SpringDataBeerRepository jpaRepository;
    private final BeerEntityMapper beerEntityMapper;

    public BeerRepositoryAdapter(SpringDataBeerRepository jpaRepository, BeerEntityMapper beerEntityMapper) {
        this.jpaRepository = jpaRepository;
        this.beerEntityMapper = beerEntityMapper;
    }

    @Override
    public Beer save(Beer beer) {
        BeerEntity entity = beerEntityMapper.toEntity(beer);
        BeerEntity savedEntity = jpaRepository.save(entity);
        return beerEntityMapper.toDomain(savedEntity);
    }

    @Override
    public Optional<Beer> findById(BeerId id) {
        return jpaRepository.findById(id.value())
                .map(beerEntityMapper::toDomain);
    }

    @Override
    public PageResult<Beer> findAll(PageRequest pageRequest) {
        Sort.Direction direction = Sort.Direction.fromString(pageRequest.direction());
        Pageable pageable = org.springframework.data.domain.PageRequest.of(
                pageRequest.page(),
                pageRequest.size(),
                Sort.by(direction, pageRequest.sortBy())
        );
        Page<BeerEntity> page = jpaRepository.findAll(pageable);
        List<Beer> beers = page
                .stream()
                .map(beerEntityMapper::toDomain)
                .toList();
        return new PageResult<>(beers, page.getNumber(),page.getSize(), page.getTotalElements());
    }

    @Override
    public void deleteById(BeerId id) {
        jpaRepository.deleteById(id.value());
    }

    @Override
    public Beer update(Beer beer) {
        BeerEntity entity = beerEntityMapper.toEntity(beer);
        BeerEntity updatedEntity = jpaRepository.save(entity);
        return beerEntityMapper.toDomain(updatedEntity);
    }

    @Override
    public PageResult<Beer> search(SearchBeerQuery query) {
        Sort.Direction direction = Sort.Direction.fromString(query.direction());

        Pageable pageable = org.springframework.data.domain.PageRequest.of(
                query.page(),
                query.size(),
                Sort.by(direction, query.sortBy())
        );

        BeerSpecificationBuilder builder = new BeerSpecificationBuilder();
        query.name().ifPresent(builder::nameContains);
        query.type().ifPresent(builder::typeEquals);
        query.manufacturerId().ifPresent(builder::manufacturerEquals);
        query.minAbv().ifPresent(builder::abvMin);
        query.maxAbv().ifPresent(builder::abvMax);

        Specification<BeerEntity> spec = builder.build();

        Page<BeerEntity> page = jpaRepository.findAll(spec, pageable);
        List<Beer> beers = page.stream()
                .map(beerEntityMapper::toDomain)
                .toList();
        return new PageResult<>(
                beers,
                page.getNumber(),
                page.getSize(),
                page.getTotalElements()
        );
    }
}

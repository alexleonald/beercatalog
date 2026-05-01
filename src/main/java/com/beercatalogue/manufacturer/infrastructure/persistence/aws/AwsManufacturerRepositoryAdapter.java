package com.beercatalogue.manufacturer.infrastructure.persistence.aws;

import com.beercatalogue.common.domain.model.PageRequest;
import com.beercatalogue.common.domain.model.PageResult;
import com.beercatalogue.manufacturer.application.port.ManufacturerRepositoryPort;
import com.beercatalogue.manufacturer.domain.model.Manufacturer;
import com.beercatalogue.manufacturer.domain.model.ManufacturerId;
import com.beercatalogue.manufacturer.infrastructure.persistence.BeerEntityMapper;
import com.beercatalogue.manufacturer.infrastructure.persistence.SpringDataManufacturerRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Profile("aws")
@Component
public class AwsManufacturerRepositoryAdapter implements ManufacturerRepositoryPort {

    private final SpringDataManufacturerRepository jpaRepository;
    private final BeerEntityMapper manufacturerEntityMapper;

    public AwsManufacturerRepositoryAdapter(SpringDataManufacturerRepository jpaRepository, BeerEntityMapper manufacturerEntityMapper) {
        this.jpaRepository = jpaRepository;
        this.manufacturerEntityMapper = manufacturerEntityMapper;
    }

    @Override
    public Manufacturer save(Manufacturer manufacturer) {
        //TODO Implement
        return null;
    }

    @Override
    public Optional<Manufacturer> findById(ManufacturerId id) {
        //TODO Implement
        return Optional.empty();
    }

    @Override
    public PageResult<Manufacturer> findAll(PageRequest pageRequest) {
        //TODO Implement
        return null;
    }

    @Override
    public void deleteById(ManufacturerId id) {
        //TODO Implement
    }

    @Override
    public Manufacturer update(Manufacturer manufacturer) {
        //TODO Implement
        return null;
    }
}

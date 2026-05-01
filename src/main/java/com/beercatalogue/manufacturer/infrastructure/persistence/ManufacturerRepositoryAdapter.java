package com.beercatalogue.manufacturer.infrastructure.persistence;

import com.beercatalogue.common.domain.model.PageRequest;
import com.beercatalogue.common.domain.model.PageResult;
import com.beercatalogue.manufacturer.application.port.ManufacturerRepositoryPort;
import com.beercatalogue.manufacturer.domain.model.Manufacturer;
import com.beercatalogue.manufacturer.domain.model.ManufacturerId;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Primary
@Component
public class ManufacturerRepositoryAdapter implements ManufacturerRepositoryPort {

    private final SpringDataManufacturerRepository jpaRepository;
    private final BeerEntityMapper manufacturerEntityMapper;

    public ManufacturerRepositoryAdapter(SpringDataManufacturerRepository jpaRepository, BeerEntityMapper manufacturerEntityMapper) {
        this.jpaRepository = jpaRepository;
        this.manufacturerEntityMapper = manufacturerEntityMapper;
    }

    @Override
    public Manufacturer save(Manufacturer manufacturer) {
        ManufacturerEntity entity = manufacturerEntityMapper.toEntity(manufacturer);
        ManufacturerEntity savedEntity = jpaRepository.save(entity);
        return manufacturerEntityMapper.toDomain(savedEntity);
    }

    @Override
    public Optional<Manufacturer> findById(ManufacturerId id) {
        return jpaRepository.findById(id.value())
                .map(manufacturerEntityMapper::toDomain);
    }

    @Override
    public PageResult<Manufacturer> findAll(PageRequest pageRequest) {
        Sort.Direction direction = Sort.Direction.fromString(pageRequest.direction());
        Pageable pageable = org.springframework.data.domain.PageRequest.of(
                pageRequest.page(),
                pageRequest.size(),
                Sort.by(direction, pageRequest.sortBy())
        );
        Page<ManufacturerEntity> page = jpaRepository.findAll(pageable);
        List<Manufacturer> manufacturers = page
                .stream()
                .map(manufacturerEntityMapper::toDomain)
                .toList();
        return new PageResult<>(manufacturers, page.getNumber(),page.getSize(), page.getTotalElements());
    }

    @Override
    public void deleteById(ManufacturerId id) {
        jpaRepository.deleteById(id.value());
    }

    @Override
    public Manufacturer update(Manufacturer manufacturer) {
        ManufacturerEntity entity = manufacturerEntityMapper.toEntity(manufacturer);
        ManufacturerEntity updatedEntity = jpaRepository.save(entity);
        return manufacturerEntityMapper.toDomain(updatedEntity);
    }
}

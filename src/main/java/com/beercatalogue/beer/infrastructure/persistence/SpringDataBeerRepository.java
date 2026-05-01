package com.beercatalogue.beer.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.UUID;

public interface SpringDataBeerRepository extends JpaRepository<BeerEntity, UUID>, JpaSpecificationExecutor<BeerEntity> {
}

package com.beercatalogue.manufacturer.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SpringDataManufacturerRepository extends JpaRepository<ManufacturerEntity, UUID> {
}

package com.beercatalogue.manufacturer.infrastructure.repository;

import com.beercatalogue.manufacturer.infrastructure.persistence.ManufacturerEntity;
import com.beercatalogue.manufacturer.infrastructure.persistence.SpringDataManufacturerRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class ManufacturerRepositoryIT {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private SpringDataManufacturerRepository repository;

    @Test
    @DisplayName("Should persist and retrieve a manufacturer by ID")
    void shouldSaveAndFindManufacturer() {
        // Arrange
        UUID manufacturerId = UUID.randomUUID();
        String ownerId = "ownerId";
        ManufacturerEntity entity = new ManufacturerEntity(manufacturerId, "Modern Brewery", "Belgium", ownerId);

        entityManager.persistAndFlush(entity);
        entityManager.clear();

        // Act
        Optional<ManufacturerEntity> found = repository.findById(entity.getId());

        // Assert
        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("Modern Brewery");
        assertThat(found.get().getCountry()).isEqualTo("Belgium");
        assertThat(found.get().getOwnerId()).isEqualTo(ownerId);
    }

    @Test
    @DisplayName("Should return empty optional when finding by non-existent ID")
    void shouldReturnEmptyWhenNotFound() {
        Optional<ManufacturerEntity> found = repository.findById(UUID.randomUUID());
        assertThat(found).isEmpty();
    }

    @Test
    @DisplayName("Should update an existing manufacturer")
    void shouldUpdateManufacturer() {
        // Arrange
        UUID manufacturerId = UUID.randomUUID();
        String ownerId = "ownerId";
        ManufacturerEntity original = new ManufacturerEntity(manufacturerId, "Old Name", "Old Country", ownerId);
        entityManager.persistAndFlush(original);
        entityManager.clear();

        // Act
        ManufacturerEntity updated = new ManufacturerEntity(manufacturerId, "New Name", "New Country", ownerId);
        repository.save(updated); // save acts as update if ID exists
        entityManager.flush();
        entityManager.clear();

        // Assert
        Optional<ManufacturerEntity> found = repository.findById(manufacturerId);
        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("New Name");
        assertThat(found.get().getCountry()).isEqualTo("New Country");
    }

    @Test
    @DisplayName("Should support pagination when fetching all manufacturers")
    void shouldReturnPaginatedManufacturers() {
        // Arrange
        for (int i = 0; i < 5; i++) {
            ManufacturerEntity entity = new ManufacturerEntity(UUID.randomUUID(), "Brewery " + i, "Country " + i, "ownerId");
            entityManager.persist(entity);
        }
        entityManager.flush();
        entityManager.clear();

        // Act
        Page<ManufacturerEntity> page = repository.findAll(PageRequest.of(0, 2));

        // Assert
        assertThat(page.getTotalElements()).isEqualTo(5);
        assertThat(page.getContent()).hasSize(2);
        assertThat(page.getTotalPages()).isEqualTo(3);
    }

    @Test
    @DisplayName("Should delete a manufacturer by ID")
    void shouldDeleteManufacturer() {
        // Arrange
        UUID manufacturerId = UUID.randomUUID();
        ManufacturerEntity entity = new ManufacturerEntity(manufacturerId, "To Delete", "None", "ownerId");
        entityManager.persistAndFlush(entity);
        entityManager.clear();

        // Act
        repository.deleteById(entity.getId());
        entityManager.flush();
        entityManager.clear();

        // Assert
        assertThat(repository.findById(entity.getId())).isEmpty();
    }
}
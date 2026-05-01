package com.beercatalogue.beer.infrastructure.repository;

import com.beercatalogue.beer.domain.model.BeerType;
import com.beercatalogue.beer.infrastructure.persistence.BeerEntity;
import com.beercatalogue.beer.infrastructure.persistence.SpringDataBeerRepository;
import com.beercatalogue.manufacturer.infrastructure.persistence.ManufacturerEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class BeerRepositoryIT {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private SpringDataBeerRepository repository;

    private ManufacturerEntity createAndPersistManufacturer() {
        ManufacturerEntity manufacturer = new ManufacturerEntity(UUID.randomUUID(), "Test Manufacturer", "Test Country", "ownerId");
        entityManager.persistAndFlush(manufacturer);
        return manufacturer;
    }

    @Test
    @DisplayName("Should save and retrieve a beer by ID")
    void shouldSaveAndFindBeer() {
        // GIVEN
        ManufacturerEntity manufacturer = createAndPersistManufacturer();
        UUID beerId = UUID.randomUUID();
        BeerEntity entity = new BeerEntity(
                beerId,
                "Grimbergen",
                new BigDecimal("6.7"),
                BeerType.ALE,
                "Dubbel",
                manufacturer.getId(),
                "http://example.com/grimbergen.jpg"
        );

        entityManager.persistAndFlush(entity);
        entityManager.clear();

        // WHEN
        Optional<BeerEntity> found = repository.findById(beerId);

        // THEN
        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("Grimbergen");
        assertThat(found.get().getAbv()).isEqualByComparingTo(new BigDecimal("6.7"));
        assertThat(found.get().getManufacturerId()).isEqualTo(manufacturer.getId());
        assertThat(found.get().getImageUrl()).isEqualTo("http://example.com/grimbergen.jpg");
    }

    @Test
    @DisplayName("Should return empty optional when finding by non-existent ID")
    void shouldReturnEmptyWhenNotFound() {
        Optional<BeerEntity> found = repository.findById(UUID.randomUUID());
        assertThat(found).isEmpty();
    }

    @Test
    @DisplayName("Should update an existing beer")
    void shouldUpdateBeer() {
        // GIVEN
        ManufacturerEntity manufacturer = createAndPersistManufacturer();
        UUID beerId = UUID.randomUUID();
        BeerEntity original = new BeerEntity(beerId, "Old Name", new BigDecimal("5.0"), BeerType.LAGER, "Old Desc", manufacturer.getId(), null);
        entityManager.persistAndFlush(original);
        entityManager.clear();

        // WHEN
        BeerEntity updated = new BeerEntity(beerId, "New Name", new BigDecimal("6.0"), BeerType.IPA, "New Desc", manufacturer.getId(), "http://new.url/img.png");
        repository.save(updated); // save acts as update if ID exists
        entityManager.flush();
        entityManager.clear();

        // THEN
        Optional<BeerEntity> found = repository.findById(beerId);
        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("New Name");
        assertThat(found.get().getAbv()).isEqualByComparingTo(new BigDecimal("6.0"));
        assertThat(found.get().getType()).isEqualTo(BeerType.IPA);
        assertThat(found.get().getDescription()).isEqualTo("New Desc");
        assertThat(found.get().getImageUrl()).isEqualTo("http://new.url/img.png");
    }

    @Test
    @DisplayName("Should support pagination when fetching all beers")
    void shouldReturnPaginatedBeers() {
        // GIVEN
        ManufacturerEntity manufacturer = createAndPersistManufacturer();
        for (int i = 0; i < 5; i++) {
            BeerEntity entity = new BeerEntity(UUID.randomUUID(), "Beer " + i, new BigDecimal("5.0"), BeerType.ALE, "Desc " + i, manufacturer.getId(), null);
            entityManager.persist(entity);
        }
        entityManager.flush();
        entityManager.clear();

        // WHEN
        Page<BeerEntity> page = repository.findAll(PageRequest.of(0, 2));

        // THEN
        assertThat(page.getTotalElements()).isEqualTo(5);
        assertThat(page.getContent()).hasSize(2);
        assertThat(page.getTotalPages()).isEqualTo(3);
    }

    @Test
    @DisplayName("Should delete a beer by ID")
    void shouldDeleteBeer() {
        // GIVEN
        ManufacturerEntity manufacturer = createAndPersistManufacturer();
        UUID beerId = UUID.randomUUID();
        BeerEntity entity = new BeerEntity(beerId, "To Delete", new BigDecimal("4.0"), BeerType.LAGER, "Desc", manufacturer.getId(), null);
        entityManager.persistAndFlush(entity);
        entityManager.clear();

        // WHEN
        repository.deleteById(beerId);
        entityManager.flush();
        entityManager.clear();

        // THEN
        assertThat(repository.findById(beerId)).isEmpty();
    }
}
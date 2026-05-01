package com.beercatalogue.manufacturer.domain;

import com.beercatalogue.manufacturer.domain.model.Manufacturer;
import com.beercatalogue.manufacturer.domain.model.ManufacturerId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ManufacturerTest {

    @Test
    @DisplayName("Should create a manufacturer with correct attributes")
    void shouldCreateManufacturerWithCorrectAttributes() {
        // Arrange
        ManufacturerId id = ManufacturerId.generate();
        String name = "Lagunitas Brewing Company";
        String country = "USA";
        String ownerId = "ownerId";

        // Act
        Manufacturer manufacturer = new Manufacturer(id, name, country, ownerId);

        // Assert
        assertThat(manufacturer).isNotNull();
        assertThat(manufacturer.getId()).isEqualTo(id);
        assertThat(manufacturer.getName()).isEqualTo(name);
        assertThat(manufacturer.getCountry()).isEqualTo(country);
        assertThat(manufacturer.getOwnerId()).isEqualTo(ownerId);
    }

}

package com.beercatalogue.manufacturer.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class ManufacturerIdTest {

    @Test
    @DisplayName("Should create from UUID")
    void shouldCreateFromUUID() {
        UUID uuid = UUID.randomUUID();
        ManufacturerId id = ManufacturerId.of(uuid);
        assertThat(id.value()).isEqualTo(uuid);
    }

    @Test
    @DisplayName("Should generate random ID")
    void shouldGenerateRandomId() {
        ManufacturerId id1 = ManufacturerId.generate();
        ManufacturerId id2 = ManufacturerId.generate();
        
        assertThat(id1).isNotNull();
        assertThat(id1.value()).isNotNull();
        assertThat(id1).isNotEqualTo(id2);
    }
}
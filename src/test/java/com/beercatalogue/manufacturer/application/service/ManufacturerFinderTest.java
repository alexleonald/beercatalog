package com.beercatalogue.manufacturer.application.service;

import com.beercatalogue.manufacturer.application.port.ManufacturerRepositoryPort;
import com.beercatalogue.manufacturer.domain.exception.ManufacturerNotFoundException;
import com.beercatalogue.manufacturer.domain.model.Manufacturer;
import com.beercatalogue.manufacturer.domain.model.ManufacturerId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ManufacturerFinderTest {

    @Mock
    private ManufacturerRepositoryPort repositoryPort;

    @InjectMocks
    private ManufacturerFinder manufacturerFinder;

    @Test
    @DisplayName("Should return manufacturer when it exists")
    void shouldReturnManufacturerWhenExists() {
        ManufacturerId id = ManufacturerId.generate();
        Manufacturer manufacturer = new Manufacturer(id, "Brewery", "Country","ownerId");
        when(repositoryPort.findById(id)).thenReturn(Optional.of(manufacturer));

        Manufacturer result = manufacturerFinder.getManufacturerByIdOrThrow(id);

        assertThat(result).isEqualTo(manufacturer);
    }

    @Test
    @DisplayName("Should throw ManufacturerNotFoundException when it does not exist")
    void shouldThrowExceptionWhenNotFound() {
        ManufacturerId id = ManufacturerId.generate();
        when(repositoryPort.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> manufacturerFinder.getManufacturerByIdOrThrow(id))
                .isInstanceOf(ManufacturerNotFoundException.class);
    }
}
package com.beercatalogue.manufacturer.application.service;

import com.beercatalogue.common.application.SecurityPort;
import com.beercatalogue.manufacturer.application.port.ManufacturerRepositoryPort;
import com.beercatalogue.manufacturer.domain.model.Manufacturer;
import com.beercatalogue.manufacturer.domain.model.ManufacturerId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DeleteManufacturerUseCaseTest {

    @Mock
    private ManufacturerRepositoryPort manufacturerRepositoryPort;

    @Mock
    private SecurityPort securityPort;

    @Mock
    private ManufacturerFinder manufacturerFinder;

    @InjectMocks
    private DeleteManufacturerService deleteManufacturerService;

    @Test
    @DisplayName("Should delete manufacturer when it exists")
    void shouldDeleteManufacturerWhenExists() {
        // Arrange
        ManufacturerId id = ManufacturerId.generate();
        Manufacturer manufacturer = new Manufacturer(id, "Brewery Alpha", "Belgium",securityPort.getCurrentUserId());

        when(manufacturerFinder.getManufacturerByIdOrThrow(id)).thenReturn(manufacturer);

        // Act
        deleteManufacturerService.deleteManufacturer(id);

        // Assert
        verify(manufacturerFinder, times(1)).getManufacturerByIdOrThrow(id);
        verify(manufacturerRepositoryPort, times(1)).deleteById(id);
    }

    @Test
    @DisplayName("Should throw exception and not call repository when manufacturer not found")
    void shouldThrowExceptionWhenNotFound() {
        // Arrange
        ManufacturerId id = ManufacturerId.generate();
        when(manufacturerFinder.getManufacturerByIdOrThrow(id)).thenThrow(new RuntimeException("Not found"));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> deleteManufacturerService.deleteManufacturer(id));
        verify(manufacturerRepositoryPort, never()).deleteById(any());
    }
}

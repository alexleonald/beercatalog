package com.beercatalogue.manufacturer.application.service;

import com.beercatalogue.common.application.SecurityPort;
import com.beercatalogue.common.domain.exception.UnauthorizedActionException;
import com.beercatalogue.manufacturer.application.ManufacturerMapper;
import com.beercatalogue.manufacturer.application.dto.CreateManufacturerRequest;
import com.beercatalogue.manufacturer.application.dto.ManufacturerResponse;
import com.beercatalogue.manufacturer.application.port.ManufacturerRepositoryPort;
import com.beercatalogue.manufacturer.domain.model.Manufacturer;
import com.beercatalogue.manufacturer.domain.model.ManufacturerId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateManufacturerServiceTest {

    @Mock
    private ManufacturerRepositoryPort repositoryPort;
    @Mock
    private ManufacturerMapper mapper;
    @Mock
    private SecurityPort securityPort;

    private CreateManufacturerService service;

    @BeforeEach
    void setUp() {
        service = new CreateManufacturerService(repositoryPort, mapper, securityPort);
    }

    @Test
    @DisplayName("Should create manufacturer when user is authenticated")
    void create_Success() {
        // GIVEN
        CreateManufacturerRequest request = new CreateManufacturerRequest("New Brewery", "Belgium");
        
        when(securityPort.getCurrentUserId()).thenReturn("userId");
        when(repositoryPort.save(any(Manufacturer.class))).thenAnswer(i -> i.getArguments()[0]);
        
        ManufacturerResponse expectedResponse = new ManufacturerResponse(ManufacturerId.generate(), "New Brewery", "Belgium");
        when(mapper.toResponse(any(Manufacturer.class))).thenReturn(expectedResponse);

        // WHEN
        ManufacturerResponse response = service.createManufacturer(request);

        // THEN
        assertThat(response).isNotNull();
        assertThat(response.name()).isEqualTo("New Brewery");
        verify(securityPort).getCurrentUserId();
        verify(repositoryPort).save(any(Manufacturer.class));
    }

    @Test
    @DisplayName("Should throw UnauthorizedActionException when user is not authenticated")
    void create_Unauthenticated_ThrowsException() {
        // GIVEN
        CreateManufacturerRequest request = new CreateManufacturerRequest("New Brewery", "Belgium");
        when(securityPort.getCurrentUserId()).thenReturn(null);

        // WHEN & THEN
        assertThrows(UnauthorizedActionException.class, () -> service.createManufacturer(request));
        verify(repositoryPort, never()).save(any());
    }
}

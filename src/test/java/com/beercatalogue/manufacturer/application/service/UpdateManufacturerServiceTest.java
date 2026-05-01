package com.beercatalogue.manufacturer.application.service;

import com.beercatalogue.common.application.SecurityPort;
import com.beercatalogue.common.domain.exception.UnauthorizedActionException;
import com.beercatalogue.manufacturer.application.ManufacturerMapper;
import com.beercatalogue.manufacturer.application.dto.ManufacturerResponse;
import com.beercatalogue.manufacturer.application.dto.UpdateManufacturerRequest;
import com.beercatalogue.manufacturer.application.port.ManufacturerRepositoryPort;
import com.beercatalogue.manufacturer.domain.model.Manufacturer;
import com.beercatalogue.manufacturer.domain.model.ManufacturerId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.beercatalogue.common.infrastructure.ApiConstants.ROLE_ADMIN;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UpdateManufacturerServiceTest {

    @Mock
    private ManufacturerRepositoryPort repositoryPort;
    @Mock
    private ManufacturerFinder manufacturerFinder;
    @Mock
    private ManufacturerMapper mapper;
    @Mock
    private SecurityPort securityPort;

    private UpdateManufacturerService service;

    @BeforeEach
    void setUp() {
        service = new UpdateManufacturerService(repositoryPort, manufacturerFinder, mapper, securityPort);
    }

    @Test
    @DisplayName("Should update manufacturer when user is owner")
    void update_WhenUserIsOwner() {
        // GIVEN
        String ownerId = "ownerId";
        ManufacturerId id = ManufacturerId.generate();
        Manufacturer existing = new Manufacturer(id, "Old Name", "Old Country", ownerId);
        UpdateManufacturerRequest request = new UpdateManufacturerRequest("New Name", "New Country");

        when(manufacturerFinder.getManufacturerByIdOrThrow(id)).thenReturn(existing);
        when(securityPort.getCurrentUserId()).thenReturn(ownerId);
        when(securityPort.hasRole(ROLE_ADMIN)).thenReturn(false);
        when(repositoryPort.update(any())).thenAnswer(i -> i.getArguments()[0]);
        when(mapper.toResponse(any())).thenReturn(mock(ManufacturerResponse.class));

        // WHEN
        service.updateManufacturer(id, request);

        // THEN
        verify(repositoryPort).update(argThat(m -> 
            m.getName().equals("New Name") && m.getCountry().equals("New Country")
        ));
    }

    @Test
    @DisplayName("Should throw exception when user is not owner and not admin")
    void update_WhenUserNotAuthorized() {
        // GIVEN
        String ownerId = "ownerId";
        String otherUserId = "otherUserId";
        ManufacturerId id = ManufacturerId.generate();
        Manufacturer existing = new Manufacturer(id, "Name", "Country", ownerId);
        UpdateManufacturerRequest request = new UpdateManufacturerRequest("", "");

        when(manufacturerFinder.getManufacturerByIdOrThrow(id)).thenReturn(existing);
        when(securityPort.getCurrentUserId()).thenReturn(otherUserId);
        when(securityPort.hasRole(ROLE_ADMIN)).thenReturn(false);

        // WHEN & THEN
        assertThrows(UnauthorizedActionException.class, () -> service.updateManufacturer(id, request));
        verify(repositoryPort, never()).update(any());
    }
}

package com.beercatalogue.beer.application.service;

import com.beercatalogue.beer.application.dto.BeerResponse;
import com.beercatalogue.beer.application.dto.UpdateBeerRequest;
import com.beercatalogue.beer.application.mapper.BeerMapper;
import com.beercatalogue.beer.application.port.BeerRepositoryPort;
import com.beercatalogue.beer.application.port.ManufacturerClientPort;
import com.beercatalogue.beer.domain.model.Beer;
import com.beercatalogue.beer.domain.model.BeerId;
import com.beercatalogue.beer.domain.model.BeerType;
import com.beercatalogue.common.application.SecurityPort;
import com.beercatalogue.common.domain.exception.UnauthorizedActionException;
import com.beercatalogue.manufacturer.domain.exception.ManufacturerNotFoundException;
import com.beercatalogue.manufacturer.domain.model.Manufacturer;
import com.beercatalogue.manufacturer.domain.model.ManufacturerId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.UUID;

import static com.beercatalogue.common.infrastructure.ApiConstants.ROLE_ADMIN;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UpdateBeerUseCaseTest {

    @Mock
    private BeerFinder beerFinder;
    @Mock
    private BeerMapper beerMapper;
    @Mock
    private BeerRepositoryPort beerRepository;
    @Mock
    private ManufacturerClientPort manufacturerClientPort;
    @Mock
    private SecurityPort securityPort;

    private UpdateBeerService updateBeerService;

    @BeforeEach
    void setUp() {
        updateBeerService = new UpdateBeerService(beerFinder, beerMapper, beerRepository, manufacturerClientPort, securityPort);
    }

    @Test
    @DisplayName("Should update all beer fields successfully when user is ADMIN (Full PUT Update)")
    void updateBeer_FullUpdate_AdminUser_Success() {
        // GIVEN
        BeerId beerId = BeerId.generate();
        ManufacturerId oldManuId = ManufacturerId.of(UUID.randomUUID());
        ManufacturerId newManuId = ManufacturerId.of(UUID.randomUUID());
        
        Beer existingBeer = new Beer(beerId, "Old Name", new BigDecimal("5.0"), BeerType.LAGER, "Old Desc", oldManuId);

        UpdateBeerRequest request = new UpdateBeerRequest(
                "New Name", 
                new BigDecimal("7.5"), 
                BeerType.IPA, 
                "New Description", 
                newManuId
        );

        when(beerFinder.getBeerByIdOrThrow(beerId)).thenReturn(existingBeer);
        when(securityPort.hasRole(ROLE_ADMIN)).thenReturn(true);
        when(manufacturerClientPort.getByIdOrThrow(newManuId)).thenReturn(mock(Manufacturer.class));
        when(beerRepository.save(existingBeer)).thenAnswer(i -> i.getArguments()[0]);
        
        BeerResponse expectedResponse = new BeerResponse(beerId, "New Name", new BigDecimal("7.5"), BeerType.IPA, "New Description", newManuId);
        when(beerMapper.toResponse(any(Beer.class))).thenReturn(expectedResponse);

        // WHEN
        BeerResponse response = updateBeerService.updateBeer(beerId, request);

        // THEN
        assertThat(response.name()).isEqualTo("New Name");
        assertThat(response.abv()).isEqualByComparingTo(new BigDecimal("7.5"));
        assertThat(response.type()).isEqualTo(BeerType.IPA);
        
        verify(beerRepository).save(argThat(beer -> 
            beer.getName().equals("New Name") &&
            beer.getAbv().equals(new BigDecimal("7.5")) &&
            beer.getType() == BeerType.IPA
        ));
    }

    @Test
    @DisplayName("Should throw UnauthorizedActionException if user is not ADMIN")
    void updateBeer_NonAdminUser_ThrowsUnauthorized() {
        // GIVEN
        BeerId beerId = BeerId.generate();
        Beer existingBeer = new Beer(beerId, "Name", new BigDecimal("5.0"), BeerType.LAGER, "Desc", ManufacturerId.of(UUID.randomUUID()));
        UpdateBeerRequest request = new UpdateBeerRequest("New", new BigDecimal("6.0"), BeerType.IPA, "New", ManufacturerId.of(UUID.randomUUID()));

        when(beerFinder.getBeerByIdOrThrow(beerId)).thenReturn(existingBeer);
        when(securityPort.hasRole(ROLE_ADMIN)).thenReturn(false);

        // WHEN & THEN
        assertThrows(UnauthorizedActionException.class, () -> updateBeerService.updateBeer(beerId, request));
        verify(beerRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should throw ManufacturerNotFoundException if new manufacturerId does not exist")
    void updateBeer_NewManufacturerNotFound_ThrowsException() {
        // GIVEN
        BeerId beerId = BeerId.generate();
        ManufacturerId newManuId = ManufacturerId.of(UUID.randomUUID());
        Beer existingBeer = new Beer(beerId, "Beer", new BigDecimal("5.0"), BeerType.LAGER, "Desc", ManufacturerId.of(UUID.randomUUID()));
        UpdateBeerRequest request = new UpdateBeerRequest("Name", new BigDecimal("5.0"), BeerType.LAGER, "Desc", newManuId);

        when(beerFinder.getBeerByIdOrThrow(beerId)).thenReturn(existingBeer);
        when(securityPort.hasRole(ROLE_ADMIN)).thenReturn(true);
        when(manufacturerClientPort.getByIdOrThrow(newManuId)).thenThrow(new ManufacturerNotFoundException("Not found"));

        // WHEN & THEN
        assertThrows(ManufacturerNotFoundException.class, () -> updateBeerService.updateBeer(beerId, request));
    }
}

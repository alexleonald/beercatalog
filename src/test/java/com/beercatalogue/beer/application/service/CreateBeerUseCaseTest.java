package com.beercatalogue.beer.application.service;

import com.beercatalogue.beer.application.dto.BeerResponse;
import com.beercatalogue.beer.application.dto.CreateBeerRequest;
import com.beercatalogue.beer.application.mapper.BeerMapper;
import com.beercatalogue.beer.application.port.BeerRepositoryPort;
import com.beercatalogue.beer.application.port.ManufacturerClientPort;
import com.beercatalogue.beer.domain.model.Beer;
import com.beercatalogue.manufacturer.domain.exception.ManufacturerNotFoundException;
import com.beercatalogue.manufacturer.domain.model.Manufacturer;
import com.beercatalogue.manufacturer.domain.model.ManufacturerId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CreateBeerUseCaseTest {

    @Mock
    private BeerRepositoryPort repositoryPort;
    @Mock
    private BeerMapper beerMapper;
    @Mock
    private ManufacturerClientPort manufacturerClientPort; // Changement ici

    private CreateBeerService createBeerService;

    @BeforeEach
    void setUp() {
        createBeerService = new CreateBeerService(repositoryPort, manufacturerClientPort, beerMapper);
    }

    @Test
    void shouldCreateBeerWhenManufacturerExists() {
        ManufacturerId manuId = new ManufacturerId(UUID.randomUUID());
        CreateBeerRequest request = new CreateBeerRequest("Stella", new BigDecimal("5.2"), null, "Lager", manuId);

        Manufacturer existingManufacturer = new Manufacturer(manuId, "Stella Artois", "Belgium","ownerId");
        when(manufacturerClientPort.getByIdOrThrow(manuId)).thenReturn(existingManufacturer);
        when(repositoryPort.save(any(Beer.class))).thenAnswer(i -> i.getArguments()[0]);
        when(beerMapper.toResponse(any())).thenReturn(new BeerResponse(null, "Stella", null, null, null, null));

        BeerResponse response = createBeerService.createBeer(request);

        assertNotNull(response);
        assertEquals("Stella", response.name());
        verify(repositoryPort).save(any());
        verify(manufacturerClientPort).getByIdOrThrow(manuId); // Vérifier l'appel au client port
    }

    @Test
    void shouldThrowExceptionWhenManufacturerDoesNotExist() {
        ManufacturerId manuId = new ManufacturerId(UUID.randomUUID());
        CreateBeerRequest request = new CreateBeerRequest("Stella", new BigDecimal("5.2"), null, "Lager", manuId);

        when(manufacturerClientPort.getByIdOrThrow(manuId))
                .thenThrow(new ManufacturerNotFoundException("Manufacturer with ID " + manuId.value() + " not found"));

        assertThrows(ManufacturerNotFoundException.class, () -> createBeerService.createBeer(request));
        verify(manufacturerClientPort).getByIdOrThrow(manuId); // Vérifier l'appel au client port
        verify(repositoryPort, never()).save(any()); // S'assurer que save n'est jamais appelé
    }
}
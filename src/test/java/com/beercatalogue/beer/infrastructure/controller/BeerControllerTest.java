package com.beercatalogue.beer.infrastructure.controller;

import com.beercatalogue.beer.application.dto.BeerResponse;
import com.beercatalogue.beer.application.dto.CreateBeerRequest;
import com.beercatalogue.beer.application.usecase.*;
import com.beercatalogue.beer.domain.model.BeerId;
import com.beercatalogue.beer.domain.model.BeerType;
import com.beercatalogue.beer.infrastructure.web.BeerController;
import com.beercatalogue.manufacturer.domain.model.ManufacturerId;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.UUID;

import static com.beercatalogue.common.infrastructure.ApiConstants.BEERS;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(
        controllers = BeerController.class,
        excludeAutoConfiguration = {
                org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class,
                org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration.class
        })
public class BeerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private CreateBeerUseCase createBeerUseCase;
    @MockitoBean
    private UpdateBeerUseCase updateBeerUseCase;
    @MockitoBean
    private GetAllBeerUseCase getAllBeerUseCase;
    @MockitoBean
    private GetBeerByIdUseCase getBeerByIdUseCase;
    @MockitoBean
    private DeleteBeerUseCase deleteBeerUseCase;
    @MockitoBean
    private SearchBeerUseCase searchBeerUseCase;
    @MockitoBean
    private AttachImageToBeerUseCase attachImageToBeerUseCase;
    @MockitoBean
    private RetrieveBeerImageUseCase retrieveBeerImageUseCase;


    @Test
    void shouldCreateBeer() throws Exception {
        UUID manuId = UUID.randomUUID();
        CreateBeerRequest request = new CreateBeerRequest("Heineken", new BigDecimal("5.0"), BeerType.LAGER, "Classic", new ManufacturerId(manuId));
        BeerResponse response = new BeerResponse(BeerId.generate(), "Heineken", new BigDecimal("5.0"), BeerType.LAGER, "Classic", new ManufacturerId(manuId));

        when(createBeerUseCase.createBeer(any())).thenReturn(response);

        mockMvc.perform(post(BEERS)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Heineken"));
    }

    @Test
    void shouldGetBeerById() throws Exception {
        UUID id = UUID.randomUUID();
        BeerResponse response = new BeerResponse(BeerId.of(id), "IPA", new BigDecimal("6.5"), null, "Strong", null);

        when(getBeerByIdUseCase.findBeerById(any())).thenReturn(response);

        mockMvc.perform(get(BEERS + "/{id}",id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("IPA"));
    }
}

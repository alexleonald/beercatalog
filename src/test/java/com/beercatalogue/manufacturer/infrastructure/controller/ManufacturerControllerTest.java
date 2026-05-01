package com.beercatalogue.manufacturer.infrastructure.controller;

import com.beercatalogue.common.domain.exception.UnauthorizedActionException;
import com.beercatalogue.manufacturer.application.dto.*;
import com.beercatalogue.manufacturer.application.usecase.*;
import com.beercatalogue.manufacturer.domain.exception.ManufacturerNotFoundException;
import com.beercatalogue.manufacturer.domain.model.ManufacturerId;
import com.beercatalogue.manufacturer.infrastructure.web.ManufacturerController;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.UUID;

import static com.beercatalogue.common.infrastructure.ApiConstants.MANUFACTURERS;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(
        controllers = ManufacturerController.class,
        excludeAutoConfiguration = {
                org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class,
                org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration.class
        })
class ManufacturerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private DeleteManufacturerUseCase deleteManufacturerUseCase;

    @MockitoBean
    private UpdateManufacturerUseCase updateManufacturerUseCase;

    @MockitoBean
    private CreateManufacturerUseCase createManufacturerUseCase;

    @MockitoBean
    private GetManufacturerByIdUseCase getManufacturerByIdUseCase;

    @MockitoBean
    private GetAllManufacturerUseCase getAllManufacturerUseCase;

    @Test
    @DisplayName("POST /api/v1/manufacturers - Should create a manufacturer and return 201")
    void createManufacturer_Success() throws Exception {
        CreateManufacturerRequest request = new CreateManufacturerRequest("Brewery A", "Germany");
        ManufacturerResponse response = new ManufacturerResponse(ManufacturerId.generate(), "Brewery A", "Germany");

        when(createManufacturerUseCase.createManufacturer(any(CreateManufacturerRequest.class))).thenReturn(response);
        mockMvc.perform(post(MANUFACTURERS)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Brewery A"));
    }

    @Test
    @DisplayName("GET /api/v1/manufacturers/{id} - Should return manufacturer when found")
    void getManufacturerById_Success() throws Exception {
        ManufacturerId id = ManufacturerId.generate();
        ManufacturerResponse response = new ManufacturerResponse(id, "Brewery A", "Germany");

        when(getManufacturerByIdUseCase.findManufacturerById(any(ManufacturerId.class))).thenReturn(response);

        mockMvc.perform(get(MANUFACTURERS + "/{id}", id.value()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id.value().toString()));
    }

    @Test
    @DisplayName("GET /api/v1/manufacturers/{id} - Should return 404 when not found")
    void getManufacturerById_NotFound() throws Exception {
        ManufacturerId id = ManufacturerId.generate();
        when(getManufacturerByIdUseCase.findManufacturerById(any(ManufacturerId.class)))
                .thenThrow(new ManufacturerNotFoundException("Not Found"));

        mockMvc.perform(get(MANUFACTURERS + "/{id}", id.value()))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("GET /api/v1/manufacturers - Should return paginated results")
    void getAllManufacturers_Success() throws Exception {
        ManufacturerPageResponse response = new ManufacturerPageResponse(Collections.emptyList(), 0, 10, 0);

        when(getAllManufacturerUseCase.findAllManufacturers(any(GetAllManufacturerQuery.class))).thenReturn(response);
        mockMvc.perform(get(MANUFACTURERS)
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("PUT /api/v1/manufacturers/{id} - Should update and return 200")
    void updateManufacturer_Success() throws Exception {
        ManufacturerId id = ManufacturerId.generate();
        UpdateManufacturerRequest request = new UpdateManufacturerRequest(
                "Updated Name",
                "Belgium"
        );
        ManufacturerResponse response = new ManufacturerResponse(id, "Updated Name", "Belgium");

        when(updateManufacturerUseCase.updateManufacturer(any(ManufacturerId.class), any(UpdateManufacturerRequest.class))).thenReturn(response);

        mockMvc.perform(put(MANUFACTURERS +"/{id}", id.value())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Name"));
    }

    @Test
    @DisplayName("PUT /api/v1/manufacturers/{id} - Should return 403 when not authorized")
    void updateManufacturer_Forbidden_NotAuthorized() throws Exception {
        ManufacturerId id = ManufacturerId.generate();
        UpdateManufacturerRequest request = new UpdateManufacturerRequest(
                "Updated Name",
                "Belgium"
        );

        when(updateManufacturerUseCase.updateManufacturer(any(ManufacturerId.class), any(UpdateManufacturerRequest.class)))
                .thenThrow(new UnauthorizedActionException("User is not authorized"));

        mockMvc.perform(put(MANUFACTURERS + "/{id}", id.value())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden()) // Attendre un 403
                .andExpect(jsonPath("$.message").value("User is not authorized")); // Vérifier le message du GlobalExceptionHandler
    }


    @Test
    @DisplayName("DELETE /api/v1/manufacturers/{id} - Should return 204")
    void deleteManufacturer_Success() throws Exception {
        UUID id = UUID.randomUUID();
        doNothing().when(deleteManufacturerUseCase).deleteManufacturer(any(ManufacturerId.class));

        mockMvc.perform(delete(MANUFACTURERS + "/{id}", id))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("DELETE /api/v1/manufacturers/{id} - Should return 403 when not authorized")
    void deleteManufacturer_Forbidden_NotAuthorized() throws Exception {
        UUID id = UUID.randomUUID();
        // Mock le use case pour qu'il lève UnauthorizedActionException
        doThrow(new UnauthorizedActionException("User is not authorized to delete"))
                .when(deleteManufacturerUseCase).deleteManufacturer(any(ManufacturerId.class));

        mockMvc.perform(delete(MANUFACTURERS+"/{id}", id))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.message").value("User is not authorized to delete"));
    }
}
package com.beercatalogue.manufacturer.infrastructure;

import com.beercatalogue.manufacturer.application.dto.CreateManufacturerRequest;
import com.beercatalogue.manufacturer.application.dto.GetAllManufacturerQuery;
import com.beercatalogue.manufacturer.application.dto.ManufacturerPageResponse;
import com.beercatalogue.manufacturer.application.usecase.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.UUID;

import static com.beercatalogue.common.infrastructure.ApiConstants.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ManufacturerSecurityIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private CreateManufacturerUseCase createManufacturerUseCase;

    @MockitoBean
    private GetAllManufacturerUseCase getAllManufacturerUseCase;

    @MockitoBean
    private GetManufacturerByIdUseCase getManufacturerByIdUseCase;

    @MockitoBean
    private UpdateManufacturerUseCase updateManufacturerUseCase;

    @MockitoBean
    private DeleteManufacturerUseCase deleteManufacturerUseCase;

    @Test
    @DisplayName("GET /api/v1/manufacturers should be accessible to everyone")
    void getManufacturers_Public() throws Exception {
        // Mocking the result to ensure it doesn't fail due to internal logic
        when(getAllManufacturerUseCase.findAllManufacturers(any(GetAllManufacturerQuery.class)))
                .thenReturn(new ManufacturerPageResponse(Collections.emptyList(), 0, 10, 0));

        mockMvc.perform(get("/api/v1/manufacturers"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("POST /api/v1/manufacturers should return 401 for anonymous users")
    void createManufacturer_Anonymous_Unauthorized() throws Exception {
        CreateManufacturerRequest request = new CreateManufacturerRequest("Brewery", "Belgium");
        
        mockMvc.perform(post(MANUFACTURERS)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = ROLE_MANUFACTURER)
    @DisplayName("POST /api/v1/manufacturers should be accessible to MANUFACTURER role")
    void createManufacturer_Authenticated_Success() throws Exception {
        CreateManufacturerRequest request = new CreateManufacturerRequest("Brewery", "Belgium");

        mockMvc.perform(post(MANUFACTURERS)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("DELETE /api/v1/manufacturers/{id} should return 401 for anonymous users")
    void deleteManufacturer_Anonymous_Unauthorized() throws Exception {
        mockMvc.perform(delete(MANUFACTURERS+"/{id}", UUID.randomUUID()))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = ROLE_ADMIN)
    @DisplayName("DELETE /api/v1/manufacturers/{id} should be accessible to ADMIN role")
    void deleteManufacturer_Admin_Success() throws Exception {

        mockMvc.perform(delete(MANUFACTURERS+"/{id}", UUID.randomUUID()))
                .andExpect(status().isNoContent());
    }
}

package com.beercatalogue.manufacturer.application.service;

import com.beercatalogue.common.domain.model.PageRequest;
import com.beercatalogue.common.domain.model.PageResult;
import com.beercatalogue.manufacturer.application.ManufacturerMapper;
import com.beercatalogue.manufacturer.application.dto.GetAllManufacturerQuery;
import com.beercatalogue.manufacturer.application.dto.ManufacturerPageResponse;
import com.beercatalogue.manufacturer.application.port.ManufacturerRepositoryPort;
import com.beercatalogue.manufacturer.domain.model.Manufacturer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetAllManufacturerServiceTest {

    @Mock
    private ManufacturerRepositoryPort repositoryPort;
    @Mock
    private ManufacturerMapper mapper;

    private GetAllManufacturerService service;

    @BeforeEach
    void setUp() {
        service = new GetAllManufacturerService(repositoryPort, mapper);
    }

    @Test
    @DisplayName("Should return paginated manufacturers")
    void findAll_Success() {
        // GIVEN
        GetAllManufacturerQuery query = new GetAllManufacturerQuery(0, 10, "name", "ASC");
        PageResult<Manufacturer> mockPage = new PageResult<>(Collections.emptyList(), 0, 10, 0);
        
        when(repositoryPort.findAll(any(PageRequest.class))).thenReturn(mockPage);
        when(mapper.toResponseList(any())).thenReturn(Collections.emptyList());

        // WHEN
        ManufacturerPageResponse response = service.findAllManufacturers(query);

        // THEN
        assertThat(response).isNotNull();
        assertThat(response.manufacturers()).isEmpty();
        assertThat(response.totalElements()).isZero();
    }
}

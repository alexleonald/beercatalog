package com.beercatalogue.manufacturer.application;

import com.beercatalogue.manufacturer.application.dto.ManufacturerResponse;
import com.beercatalogue.manufacturer.domain.model.Manufacturer;
import com.beercatalogue.manufacturer.domain.model.ManufacturerId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class ManufacturerMapperTest {

    private ManufacturerMapper manufacturerMapper;

    @BeforeEach
    void setUp() {
        manufacturerMapper = new ManufacturerMapperImpl();
    }

    @Test
    void toResponse() {
        ManufacturerId id = new ManufacturerId(UUID.randomUUID());
        Manufacturer domain = new Manufacturer(id, "Brewery C", "Country C","ownerId");
        ManufacturerResponse response = manufacturerMapper.toResponse(domain);

        assertThat(response).isNotNull();
        assertThat(response.id()).isEqualTo(id);
        assertThat(response.name()).isEqualTo("Brewery C");
        assertThat(response.country()).isEqualTo("Country C");
    }

    @Test
    void toResponseList() {
        ManufacturerId id1 = new ManufacturerId(UUID.randomUUID());
        ManufacturerId id2 = new ManufacturerId(UUID.randomUUID());
        Manufacturer domain1 = new Manufacturer(id1, "Brewery D", "Country D","ownerId");
        Manufacturer domain2 = new Manufacturer(id2, "Brewery E", "Country E","ownerId");
        List<Manufacturer> domainList = Arrays.asList(domain1, domain2);

        List<ManufacturerResponse> responseList = manufacturerMapper.toResponseList(domainList);

        assertThat(responseList).isNotNull().hasSize(2);
        assertThat(responseList.get(0).id()).isEqualTo(id1);
        assertThat(responseList.get(1).name()).isEqualTo("Brewery E");
    }
}

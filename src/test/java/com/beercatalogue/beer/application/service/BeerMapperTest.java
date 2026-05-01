package com.beercatalogue.beer.application.service;

import com.beercatalogue.beer.application.dto.BeerResponse;
import com.beercatalogue.beer.application.mapper.BeerMapper;
import com.beercatalogue.beer.application.mapper.BeerMapperImpl;
import com.beercatalogue.beer.domain.model.Beer;
import com.beercatalogue.beer.domain.model.BeerId;
import com.beercatalogue.beer.domain.model.BeerType;
import com.beercatalogue.manufacturer.domain.model.ManufacturerId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class BeerMapperTest {

    private BeerMapper beerMapper;

    @BeforeEach
    void setUp() {
        beerMapper = new BeerMapperImpl();
    }

    @Test
    void toResponse() {
        BeerId id = BeerId.generate();
        ManufacturerId manuId = new ManufacturerId(UUID.randomUUID());
        Beer domain = new Beer(
                id, "Stout", new BigDecimal("7.0"), BeerType.STOUT, "Dark and rich", manuId
        );
        BeerResponse response = beerMapper.toResponse(domain);

        assertThat(response).isNotNull();
        assertThat(response.id()).isEqualTo(id);
        assertThat(response.name()).isEqualTo("Stout");
        assertThat(response.abv()).isEqualByComparingTo(new BigDecimal("7.0"));
        assertThat(response.type()).isEqualTo(BeerType.STOUT);
        assertThat(response.description()).isEqualTo("Dark and rich");
        assertThat(response.manufacturerId()).isEqualTo(manuId);
    }

    @Test
    void toResponseList() {
        BeerId id1 = BeerId.generate();
        BeerId id2 = BeerId.generate();
        ManufacturerId manuId1 = new ManufacturerId(UUID.randomUUID());
        ManufacturerId manuId2 = new ManufacturerId(UUID.randomUUID());

        Beer domain1 = new Beer(id1, "Ale", new BigDecimal("5.5"), BeerType.LAGER, "Fruity", manuId1);
        Beer domain2 = new Beer(id2, "Lager", new BigDecimal("4.0"), BeerType.LAGER, "Light", manuId2);
        List<Beer> domainList = Arrays.asList(domain1, domain2);

        List<BeerResponse> responseList = beerMapper.toResponseList(domainList);

        assertThat(responseList).isNotNull().hasSize(2);
        assertThat(responseList.get(0).id()).isEqualTo(id1);
        assertThat(responseList.get(1).name()).isEqualTo("Lager");
    }
}

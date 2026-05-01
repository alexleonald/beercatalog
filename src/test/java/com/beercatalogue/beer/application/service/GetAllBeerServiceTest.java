package com.beercatalogue.beer.application.service;

import com.beercatalogue.beer.application.dto.BeerPageResponse;
import com.beercatalogue.beer.application.dto.GetAllBeerQuery;
import com.beercatalogue.beer.application.mapper.BeerMapper;
import com.beercatalogue.beer.application.port.BeerRepositoryPort;
import com.beercatalogue.beer.domain.model.Beer;
import com.beercatalogue.common.domain.model.PageRequest;
import com.beercatalogue.common.domain.model.PageResult;
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
class GetAllBeerServiceTest {

    @Mock
    private BeerRepositoryPort repositoryPort;
    @Mock
    private BeerMapper mapper;

    private GetAllBeerService service;

    @BeforeEach
    void setUp() {
        service = new GetAllBeerService(repositoryPort, mapper);
    }

    @Test
    @DisplayName("Should return paginated beers")
    void findAll_Success() {
        // GIVEN
        GetAllBeerQuery query = new GetAllBeerQuery(0, 10, "name", "ASC");
        PageResult<Beer> mockPage = new PageResult<>(Collections.emptyList(), 0, 10, 0);

        when(repositoryPort.findAll(any(PageRequest.class))).thenReturn(mockPage);
        when(mapper.toResponseList(any())).thenReturn(Collections.emptyList());

        // WHEN
        BeerPageResponse response = service.findAllBeers(query);

        // THEN
        assertThat(response).isNotNull();
        assertThat(response.beers()).isEmpty();
    }
}

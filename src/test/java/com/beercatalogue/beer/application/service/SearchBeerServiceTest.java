package com.beercatalogue.beer.application.service;

import com.beercatalogue.beer.application.dto.BeerPageResponse;
import com.beercatalogue.beer.application.dto.SearchBeerQuery;
import com.beercatalogue.beer.application.mapper.BeerMapper;
import com.beercatalogue.beer.application.port.BeerRepositoryPort;
import com.beercatalogue.beer.domain.model.Beer;
import com.beercatalogue.beer.domain.model.BeerType;
import com.beercatalogue.common.domain.model.PageResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SearchBeerServiceTest {

    @Mock
    private BeerRepositoryPort repositoryPort;
    @Mock
    private BeerMapper mapper;

    private SearchBeerService service;

    @BeforeEach
    void setUp() {
        service = new SearchBeerService(repositoryPort, mapper);
    }

    @Test
    @DisplayName("Should return matching beers for search query")
    void search_Success() {
        // GIVEN
        SearchBeerQuery query = new SearchBeerQuery(
                0,
                10,
                "name",
                "ASC",
                Optional.of("Pils"),
                Optional.of(BeerType.LAGER),
                Optional.of(BigDecimal.ZERO),
                Optional.of(BigDecimal.TEN),
                Optional.of(UUID.randomUUID())
        );
        PageResult<Beer> mockPage = new PageResult<>(Collections.emptyList(), 0, 10, 0);

        when(repositoryPort.search(query)).thenReturn(mockPage);
        when(mapper.toResponseList(any())).thenReturn(Collections.emptyList());

        // WHEN
        BeerPageResponse response = service.searchBeers(query);

        // THEN
        assertThat(response).isNotNull();
    }
}

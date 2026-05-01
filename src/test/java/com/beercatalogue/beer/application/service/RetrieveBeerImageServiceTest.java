package com.beercatalogue.beer.application.service;

import com.beercatalogue.beer.application.dto.BeerImageResponse;
import com.beercatalogue.beer.domain.model.Beer;
import com.beercatalogue.beer.domain.model.BeerId;
import com.beercatalogue.beer.domain.model.ImageFile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RetrieveBeerImageServiceTest {

    @Mock
    private BeerFinder beerFinder;

    private RetrieveBeerImageService service;

    @BeforeEach
    void setUp() {
        service = new RetrieveBeerImageService(beerFinder);
    }

    @Test
    @DisplayName("Should retrieve beer image successfully without an image")
    void retrieve_Success_without_an_image() {
        // GIVEN
        BeerId beerId = BeerId.generate();
        Beer mockBeer = mock(Beer.class);
        
        when(beerFinder.getBeerByIdOrThrow(beerId)).thenReturn(mockBeer);

        // WHEN
        BeerImageResponse response = service.retrieveBeerImage(beerId);

        // THEN
        assertThat(response).isNotNull();
        assertThat(response.imageUrl()).isEqualTo(null);
    }

    @Test
    @DisplayName("Should retrieve beer image successfully with an image")
    void retrieve_Success_with_an_Image() {
        // GIVEN
        BeerId beerId = BeerId.generate();
        Beer mockBeer = mock(Beer.class);

        when(beerFinder.getBeerByIdOrThrow(beerId)).thenReturn(mockBeer);
        when(mockBeer.getImageFile()).thenReturn(ImageFile.of("http://url.com"));

        // WHEN
        BeerImageResponse response = service.retrieveBeerImage(beerId);

        // THEN
        assertThat(response).isNotNull();
        assertThat(response.imageUrl()).isEqualTo("http://url.com");
    }
}

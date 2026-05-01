package com.beercatalogue.beer.application.service;

import com.beercatalogue.beer.application.port.BeerRepositoryPort;
import com.beercatalogue.beer.application.usecase.DeleteBeerUseCase;
import com.beercatalogue.beer.domain.model.BeerId;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class DeleteBeerService implements DeleteBeerUseCase {

	private final BeerRepositoryPort beerRepository;
    private final BeerFinder beerFinder;

	public DeleteBeerService(BeerRepositoryPort beerRepository, BeerFinder beerFinder) {
		this.beerRepository = beerRepository;
        this.beerFinder = beerFinder;
    }

	@Transactional
    @Override
	public void deleteBeer(BeerId id) {
        log.info("Attempting to delete beer with ID: {}", id.value());
        beerFinder.getBeerByIdOrThrow(id);
		beerRepository.deleteById(id);
        log.info("Beer with ID {} deleted successfully.", id.value());
	}
}

package com.beercatalogue.beer.domain.exception;

public class BeerNotFoundException extends RuntimeException {
    public BeerNotFoundException(String s) {
        super(s);
    }
}

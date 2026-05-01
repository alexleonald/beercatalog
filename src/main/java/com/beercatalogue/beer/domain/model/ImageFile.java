package com.beercatalogue.beer.domain.model;

public record ImageFile(
        String imageUrl
) {
    public static ImageFile of(String imageUrl) {
        //perform checks on imageUrl
        return new ImageFile(imageUrl);
    }
}

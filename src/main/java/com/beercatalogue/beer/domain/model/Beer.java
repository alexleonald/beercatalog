package com.beercatalogue.beer.domain.model;

import com.beercatalogue.manufacturer.domain.model.ManufacturerId;

import java.math.BigDecimal;

public class Beer {
    private BeerId id;
    private String name;
    private BigDecimal abv;
    private BeerType type;
    private String description;
    private ManufacturerId manufacturerId;
    private ImageFile imageFile;



    public Beer(BeerId id, String name, BigDecimal abv, BeerType type, String description, ManufacturerId manufacturerId, ImageFile imageFile) {
        this.id = id;
        this.name = name;
        this.abv = abv;
        this.type = type;
        this.description = description;
        this.manufacturerId = manufacturerId;
        this.imageFile = imageFile;
    }

    public Beer(BeerId id, String name, BigDecimal abv, BeerType type, String description, ManufacturerId manufacturerId) {
        this.id = id;
        this.name = name;
        this.abv = abv;
        this.type = type;
        this.description = description;
        this.manufacturerId = manufacturerId;
        this.imageFile = null;
    }

    public BeerId getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getAbv() {
        return abv;
    }

    public BeerType getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }

    public ManufacturerId getManufacturerId() {
        return manufacturerId;
    }

    public ImageFile getImageFile() {
        return imageFile;
    }

    public void updateBeerDetails(String name, BigDecimal abv, BeerType type, String description, ManufacturerId manufacturerId){
        this.name = name;
        this.abv = abv;
        this.type = type;
        this.description = description;
        this.manufacturerId = manufacturerId;
    }

    public void attachImage(ImageFile imageFile) {
        this.imageFile = imageFile;
    }
}

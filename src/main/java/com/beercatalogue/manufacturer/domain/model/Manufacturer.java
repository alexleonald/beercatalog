package com.beercatalogue.manufacturer.domain.model;

public class Manufacturer {
    private ManufacturerId id;
    private String name;
    private String country;
    private String ownerId;

    public Manufacturer(ManufacturerId id, String name, String country, String ownerId) {
        this.id = id;
        this.name = name;
        this.country = country;
        this.ownerId = ownerId;
    }

    public ManufacturerId getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCountry() {
        return country;
    }

    public String getOwnerId() {
        return ownerId;
    }
}

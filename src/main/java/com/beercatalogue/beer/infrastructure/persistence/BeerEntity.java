package com.beercatalogue.beer.infrastructure.persistence;

import com.beercatalogue.beer.domain.model.BeerType;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "beers")
public class BeerEntity {

    @Id
    private UUID id;
    
    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private BigDecimal abv;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BeerType type;

    private String description;

    @Column(name = "manufacturer_id", nullable = false)
    private UUID manufacturerId;

    @Column(name = "image_url")
    private String imageUrl;

    public BeerEntity() {
    }

    public BeerEntity(UUID id, String name, BigDecimal abv, BeerType type, String description, UUID manufacturerId, String imageUrl) {
        this.id = id;
        this.name = name;
        this.abv = abv;
        this.type = type;
        this.description = description;
        this.manufacturerId = manufacturerId;
        this.imageUrl = imageUrl;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getAbv() {
        return abv;
    }
    public void setAbv(BigDecimal abv) {
        this.abv = abv;
    }

    public BeerType getType() {
        return type;
    }
    public void setType(BeerType type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public UUID getManufacturerId() {
        return manufacturerId;
    }
    public void setManufacturerId(UUID manufacturerId) {
        this.manufacturerId = manufacturerId;
    }

    public String getImageUrl() {
        return imageUrl;
    }
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}

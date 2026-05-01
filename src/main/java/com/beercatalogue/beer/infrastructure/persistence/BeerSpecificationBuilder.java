package com.beercatalogue.beer.infrastructure.persistence;

import com.beercatalogue.beer.domain.model.BeerType;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.util.UUID;

public class BeerSpecificationBuilder {

    private Specification<BeerEntity> spec;

    public BeerSpecificationBuilder() {
        this.spec = null;
    }

    public BeerSpecificationBuilder nameContains(String name) {
        spec = and(spec, (root, query, cb) ->
                cb.like(cb.lower(root.get("name")),
                        "%" + name.toLowerCase() + "%"));
        return this;
    }

    public BeerSpecificationBuilder typeEquals(BeerType type) {
        spec = and(spec, (root, query, cb) ->
                cb.equal(root.get("type"), type));
        return this;
    }

    public BeerSpecificationBuilder manufacturerEquals(UUID id) {
        spec = and(spec, (root, query, cb) ->
                cb.equal(root.get("manufacturerId"), id));
        return this;
    }

    public BeerSpecificationBuilder abvMin(BigDecimal min) {
        spec = and(spec, (root, query, cb) ->
                cb.greaterThanOrEqualTo(root.get("abv"), min));
        return this;
    }

    public BeerSpecificationBuilder abvMax(BigDecimal max) {
        spec = and(spec, (root, query, cb) ->
                cb.lessThanOrEqualTo(root.get("abv"), max));
        return this;
    }

    public Specification<BeerEntity> build() {
        return spec ;
    }

    private Specification<BeerEntity> and(
            Specification<BeerEntity> base,
            Specification<BeerEntity> next
    ) {
        return base == null ? next : base.and(next);
    }
}

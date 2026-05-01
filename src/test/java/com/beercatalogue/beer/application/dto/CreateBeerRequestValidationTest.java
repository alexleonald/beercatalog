package com.beercatalogue.beer.application.dto;

import com.beercatalogue.beer.domain.model.BeerType;
import com.beercatalogue.manufacturer.domain.model.ManufacturerId;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class CreateBeerRequestValidationTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void shouldValidateSuccessfullyWithValidRequest() {
        CreateBeerRequest request = new CreateBeerRequest(
                "IPA", new BigDecimal("6.5"), BeerType.IPA, "Hoppy and bitter", new ManufacturerId(UUID.randomUUID())
        );
        Set<ConstraintViolation<CreateBeerRequest>> violations = validator.validate(request);
        assertThat(violations).isEmpty();
    }

    @Test
    void shouldFailValidationWhenNameIsNull() {
        CreateBeerRequest request = new CreateBeerRequest(
                null, new BigDecimal("6.5"), BeerType.IPA, "Hoppy and bitter", new ManufacturerId(UUID.randomUUID())
        );
        Set<ConstraintViolation<CreateBeerRequest>> violations = validator.validate(request);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("Name is required");
    }

    @Test
    void shouldFailValidationWhenNameIsBlank() {
        CreateBeerRequest request = new CreateBeerRequest(
                " ", new BigDecimal("6.5"), BeerType.IPA, "Hoppy and bitter", new ManufacturerId(UUID.randomUUID())
        );
        Set<ConstraintViolation<CreateBeerRequest>> violations = validator.validate(request);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("Name is required");
    }

    @Test
    void shouldFailValidationWhenAbvIsNull() {
        CreateBeerRequest request = new CreateBeerRequest(
                "IPA", null, BeerType.IPA, "Hoppy and bitter", new ManufacturerId(UUID.randomUUID())
        );
        Set<ConstraintViolation<CreateBeerRequest>> violations = validator.validate(request);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("ABV is required");
    }

    @Test
    void shouldFailValidationWhenAbvIsNegative() {
        CreateBeerRequest request = new CreateBeerRequest(
                "IPA", new BigDecimal("-1.0"), BeerType.IPA, "Hoppy and bitter", new ManufacturerId(UUID.randomUUID())
        );
        Set<ConstraintViolation<CreateBeerRequest>> violations = validator.validate(request);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("ABV cannot be negative");
    }

    @Test
    void shouldFailValidationWhenTypeIsNull() {
        CreateBeerRequest request = new CreateBeerRequest(
                "IPA", new BigDecimal("6.5"), null, "Hoppy and bitter", new ManufacturerId(UUID.randomUUID())
        );
        Set<ConstraintViolation<CreateBeerRequest>> violations = validator.validate(request);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("BeerType is required");
    }

    @Test
    void shouldFailValidationWhenManufacturerIdIsNull() {
        CreateBeerRequest request = new CreateBeerRequest(
                "IPA", new BigDecimal("6.5"), BeerType.IPA, "Hoppy and bitter", null
        );
        Set<ConstraintViolation<CreateBeerRequest>> violations = validator.validate(request);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("Manufacturer ID is required");
    }
}

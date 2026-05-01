package com.beercatalogue.manufacturer.application.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class CreateManufacturerRequestValidationTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void shouldValidateSuccessfullyWithValidRequest() {
        CreateManufacturerRequest request = new CreateManufacturerRequest("Brewery X", "USA");
        Set<ConstraintViolation<CreateManufacturerRequest>> violations = validator.validate(request);
        assertThat(violations).isEmpty();
    }

    @Test
    void shouldFailValidationWhenNameIsNull() {
        CreateManufacturerRequest request = new CreateManufacturerRequest(null, "USA");
        Set<ConstraintViolation<CreateManufacturerRequest>> violations = validator.validate(request);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("Name is required");
    }

    @Test
    void shouldFailValidationWhenNameIsBlank() {
        CreateManufacturerRequest request = new CreateManufacturerRequest(" ", "USA");
        Set<ConstraintViolation<CreateManufacturerRequest>> violations = validator.validate(request);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("Name is required");
    }

    @Test
    void shouldFailValidationWhenCountryIsNull() {
        CreateManufacturerRequest request = new CreateManufacturerRequest("Brewery X", null);
        Set<ConstraintViolation<CreateManufacturerRequest>> violations = validator.validate(request);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("Country is required");
    }

    @Test
    void shouldFailValidationWhenCountryIsBlank() {
        CreateManufacturerRequest request = new CreateManufacturerRequest("Brewery X", " ");
        Set<ConstraintViolation<CreateManufacturerRequest>> violations = validator.validate(request);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("Country is required");
    }
}

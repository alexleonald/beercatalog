package com.beercatalogue.beer.infrastructure.web;

import com.beercatalogue.beer.domain.exception.BeerNotFoundException;
import com.beercatalogue.manufacturer.domain.exception.ManufacturerNotFoundException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class BeerExceptionHandler {

    @ExceptionHandler(BeerNotFoundException.class)
    public ResponseEntity<ProblemDetail> handleBeerNotFound(ManufacturerNotFoundException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
        problemDetail.setTitle("Beer Not Found");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(problemDetail);
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ProblemDetail> handleValidation(MethodArgumentNotValidException ex) {

        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
        );
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        problemDetail.setProperty("errors", errors);

        return ResponseEntity.badRequest().body(problemDetail);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ProblemDetail> handleEnumParseError(HttpMessageNotReadableException ex) {
        Throwable cause = ex.getCause();
        if (cause instanceof InvalidFormatException ife
                && ife.getTargetType() != null
                && ife.getTargetType().isEnum()) {
            Class<?> enumClass = ife.getTargetType();
            String invalidValue = String.valueOf(ife.getValue());
            String allowedValues = Arrays.stream(enumClass.getEnumConstants())
                    .map(Object::toString)
                    .collect(Collectors.joining(", "));

            ProblemDetail pd = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
            pd.setTitle("Invalid enum value");
            pd.setDetail(invalidValue + " is not valid. Allowed values: " + allowedValues);

            return ResponseEntity.badRequest().body(pd);
        }

        ProblemDetail pd = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        pd.setTitle("Invalid request body");
        pd.setDetail("Malformed JSON");

        return ResponseEntity.badRequest().body(pd);
    }
}
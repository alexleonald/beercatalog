package com.beercatalogue.manufacturer.infrastructure.web;

import com.beercatalogue.manufacturer.application.dto.*;
import com.beercatalogue.manufacturer.application.usecase.*;
import com.beercatalogue.manufacturer.domain.model.ManufacturerId;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import static com.beercatalogue.common.infrastructure.ApiConstants.MANUFACTURERS;

@RestController
@RequestMapping(MANUFACTURERS)
@Tag(name = "Manufacturers")
public class ManufacturerController {

    private final DeleteManufacturerUseCase deleteManufacturerUseCase;
    private final UpdateManufacturerUseCase updateManufacturerUseCase;
    private final CreateManufacturerUseCase createManufacturerUseCase;
    private final GetManufacturerByIdUseCase getManufacturerByIdUseCase;
    private final GetAllManufacturerUseCase getAllManufacturerUseCase;

    public ManufacturerController(
            DeleteManufacturerUseCase deleteManufacturerUseCase,
            UpdateManufacturerUseCase updateManufacturerUseCase,
            CreateManufacturerUseCase createManufacturerUseCase,
            GetManufacturerByIdUseCase getManufacturerByIdUseCase, GetAllManufacturerUseCase getAllManufacturerUseCase) {
        this.deleteManufacturerUseCase = deleteManufacturerUseCase;
        this.updateManufacturerUseCase = updateManufacturerUseCase;
        this.createManufacturerUseCase = createManufacturerUseCase;
        this.getManufacturerByIdUseCase = getManufacturerByIdUseCase;
        this.getAllManufacturerUseCase = getAllManufacturerUseCase;
    }

    @Operation(summary = "Crate a new manufacturer", responses = {
            @ApiResponse(responseCode = "201", description = "Manufacturer created successfully"),
            @ApiResponse(responseCode = "400", description = "Bad request")
    })
    @PostMapping
    public ResponseEntity<ManufacturerResponse> createManufacturer(@RequestBody @Valid CreateManufacturerRequest request) {
        ManufacturerResponse response = createManufacturerUseCase.createManufacturer(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Get a manufacturer by ID")
    @ApiResponse(responseCode = "200", description = "Manufacturer found")
    @ApiResponse(responseCode = "404", description = "Manufacturer not found")
    @GetMapping("/{id}")
    public ResponseEntity<ManufacturerResponse> getManufacturerById(@PathVariable UUID id) {
        ManufacturerResponse response = getManufacturerByIdUseCase.findManufacturerById(ManufacturerId.of(id));
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Find all Manufacturers with pagination and sorting")
    @GetMapping
    public ResponseEntity<ManufacturerPageResponse> getAllManufacturers(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String direction
    ) {
        GetAllManufacturerQuery query = GetAllManufacturerQuery.of(page,size,sortBy,direction);
        ManufacturerPageResponse response = getAllManufacturerUseCase.findAllManufacturers(query);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Update a manufacturer")
    @ApiResponse(responseCode = "200", description = "Manufacturer updated")
    @ApiResponse(responseCode = "404", description = "Manufacturer not found")
    @PutMapping("/{id}")
    public ResponseEntity<ManufacturerResponse> updateManufacturer(
            @PathVariable UUID id,
            @RequestBody @Valid UpdateManufacturerRequest request) {
        ManufacturerResponse response = updateManufacturerUseCase.updateManufacturer(ManufacturerId.of(id), request);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Delete a manufacturer by ID")
    @ApiResponse(responseCode = "204", description = "Manufacturer deleted successfully")
    @ApiResponse(responseCode = "404", description = "Manufacturer not found")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteManufacturer(@PathVariable UUID id) {
        deleteManufacturerUseCase.deleteManufacturer(ManufacturerId.of(id));
        return ResponseEntity.noContent().build();
    }
}

package com.beercatalogue.beer.infrastructure.web;

import com.beercatalogue.beer.application.dto.*;
import com.beercatalogue.beer.application.usecase.*;
import com.beercatalogue.beer.domain.model.BeerId;
import com.beercatalogue.beer.domain.model.BeerType;
import com.beercatalogue.beer.domain.model.ImageFile;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.UUID;

import static com.beercatalogue.common.infrastructure.ApiConstants.BEERS;

@RestController
@RequestMapping(BEERS)
@Tag(name = "Beers")
public class BeerController {

    private final CreateBeerUseCase createBeerUseCase;
    private final UpdateBeerUseCase updateBeerUseCase;
    private final GetAllBeerUseCase getAllBeerUseCase;
    private final GetBeerByIdUseCase getBeerByIdUseCase;
    private final DeleteBeerUseCase deleteBeerUseCase ;
    private final SearchBeerUseCase searchBeerUseCase;
    private final AttachImageToBeerUseCase attachImageToBeerUseCase;
    private final RetrieveBeerImageUseCase retrieveBeerImageUseCase;


    public BeerController(CreateBeerUseCase createBeerUseCase, UpdateBeerUseCase updateBeerUseCase, GetAllBeerUseCase getAllBeerUseCase, GetBeerByIdUseCase getBeerByIdUseCase, DeleteBeerUseCase deleteBeerUseCase, SearchBeerUseCase searchBeerUseCase, AttachImageToBeerUseCase attachImageToBeerUseCase, RetrieveBeerImageUseCase retrieveBeerImageUseCase) {
        this.createBeerUseCase = createBeerUseCase;
        this.updateBeerUseCase = updateBeerUseCase;
        this.getAllBeerUseCase = getAllBeerUseCase;
        this.getBeerByIdUseCase = getBeerByIdUseCase;
        this.deleteBeerUseCase = deleteBeerUseCase;
        this.searchBeerUseCase = searchBeerUseCase;
        this.attachImageToBeerUseCase = attachImageToBeerUseCase;
        this.retrieveBeerImageUseCase = retrieveBeerImageUseCase;
    }

    @Operation(summary = "Crate a new beer", responses = {
            @ApiResponse(responseCode = "201", description = "beer created successfully"),
            @ApiResponse(responseCode = "400", description = "Bad request")
    })
    @PostMapping
    public ResponseEntity<BeerResponse> createBeer(@RequestBody @Valid CreateBeerRequest createBeerRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(createBeerUseCase.createBeer(createBeerRequest));
    }

    @Operation(summary = "Update a beer bu its ID")
    @ApiResponse(responseCode = "200", description = "beer updated")
    @ApiResponse(responseCode = "404", description = "beer not found")
    @PutMapping("/{id}")
    public ResponseEntity<BeerResponse> updateBeer(
            @PathVariable UUID id,
            @RequestBody @Valid UpdateBeerRequest updateBeerRequest) {
        return ResponseEntity.ok(updateBeerUseCase.updateBeer(BeerId.of(id), updateBeerRequest));
    }

    @Operation(summary = "Get a beer by Id")
    @ApiResponse(responseCode = "200", description = "beer found")
    @ApiResponse(responseCode = "404", description = "beer not found")
    @GetMapping("/{id}")
     public ResponseEntity<BeerResponse> getBeerById(UUID id) {
        return ResponseEntity.ok(getBeerByIdUseCase.findBeerById(BeerId.of(id)));
    }

    @Operation(summary = "Find all beers with pagination and sorting")
    @GetMapping
    public ResponseEntity<BeerPageResponse> getAllBeers(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String direction
    ) {
        GetAllBeerQuery query = GetAllBeerQuery.of(page, size, sortBy, direction);
        return ResponseEntity.ok(getAllBeerUseCase.findAllBeers(query));
    }


    @Operation(summary = "Delete beer by its ID")
    @ApiResponse(responseCode = "204", description = "Manufacturer deleted successfully")
    @ApiResponse(responseCode = "404", description = "Manufacturer not found")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBeer(UUID id) {
        deleteBeerUseCase.deleteBeer(BeerId.of(id));
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Search beers with filters and pagination and sorting")
    @ApiResponse(responseCode = "200", description = "Beers found")
    @ApiResponse(responseCode = "400", description = "Bad request")
    @GetMapping("/search")
    public ResponseEntity<BeerPageResponse> search(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String direction,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) BeerType type,
            @RequestParam(required = false) BigDecimal minAbv,
            @RequestParam(required = false) BigDecimal maxAbv,
            @RequestParam(required = false) UUID manufacturerId
    ) {
        SearchBeerQuery query = SearchBeerQuery.of(
                page, size, sortBy, direction,
                name, type, minAbv, maxAbv, manufacturerId
        );

        return ResponseEntity.ok(searchBeerUseCase.searchBeers(query));
    }

    @Operation(summary = "Attach an image to a beer")
    @ApiResponse(responseCode = "200", description = "Image attached successfully")
    @ApiResponse(responseCode = "404", description = "Beer not found")
    @PostMapping("/{id}/image")
    public ResponseEntity<BeerImageResponse> attachImageToBeer(
            @PathVariable UUID id,
            @RequestBody String imageUrl
    ) {
        ImageFile imageFile = ImageFile.of(imageUrl);
        BeerImageResponse response = attachImageToBeerUseCase.attachImageToBeer(BeerId.of(id), imageFile);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Retrieve the image of a beer")
    @ApiResponse(responseCode = "200", description = "Image retrieved successfully")
    @ApiResponse(responseCode = "404", description = "Beer not found")
    @GetMapping("/{id}/image")
    public ResponseEntity<BeerImageResponse> retrieveBeerImage(@PathVariable UUID id) {
        BeerImageResponse response = retrieveBeerImageUseCase.retrieveBeerImage(BeerId.of(id));
        return ResponseEntity.ok(response);
    }

}

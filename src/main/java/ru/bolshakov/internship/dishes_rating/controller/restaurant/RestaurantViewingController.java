package ru.bolshakov.internship.dishes_rating.controller.restaurant;

import io.swagger.annotations.*;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.bolshakov.internship.dishes_rating.dto.restaurant.RestaurantDTO;
import ru.bolshakov.internship.dishes_rating.dto.search.RestaurantSearchRequest;
import ru.bolshakov.internship.dishes_rating.service.RestaurantService;

import java.util.List;

@RestController
@RequestMapping("/restaurants")
public class RestaurantViewingController {

    private final RestaurantService restaurantService;

    public RestaurantViewingController(RestaurantService restaurantService) {
        this.restaurantService = restaurantService;
    }

    @ApiOperation(value = "Get all existent restaurants")
    @ApiResponse(code = 200, message = "Getting all was successful. Return restaurants with theirs rating.")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNumber", dataType = "integer", paramType = "query",
                    value = "Results page you want to retrieve (0...N)."),
            @ApiImplicitParam(name = "pageSize", dataType = "integer", paramType = "query",
                    value = "Number of records per page. MAX value is defined in configuration. " +
                            "If the request contains number greater than MAX value, " +
                            "it will be shown only as many elements per page, as allowed"),
            @ApiImplicitParam(name = "sort", allowMultiple = true, dataType = "string", paramType = "query",
                    value = "Sorting criteria in the format: property(,asc|desc). " +
                            "Default sort order is ascending. " +
                            "Multiple sort criteria are supported.")})
    @GetMapping
    public ResponseEntity<List<RestaurantDTO>> getAll(Pageable pageable,
                                                      RestaurantSearchRequest restaurantSearchRequest) {
        List<RestaurantDTO> returnedRestaurants = restaurantService.getAllWithRatingByDate(pageable, restaurantSearchRequest);
        return ResponseEntity.ok(returnedRestaurants);
    }

    @ApiOperation(value = "Get existent restaurant by ID")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Getting was successful. Return restaurant by ID with rating."),
            @ApiResponse(code = 404, message = "Restaurant with such ID not found")})
    @GetMapping("/{restaurantId}")
    public ResponseEntity<RestaurantDTO> get(@PathVariable @Parameter(in = ParameterIn.PATH, description = "ID of restaurant") Long restaurantId) {
        RestaurantDTO returnedRestaurant = restaurantService.getRestaurantWithRatingByDate(restaurantId);
        return ResponseEntity.ok(returnedRestaurant);
    }
}
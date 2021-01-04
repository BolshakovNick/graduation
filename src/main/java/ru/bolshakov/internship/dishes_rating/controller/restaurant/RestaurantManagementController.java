package ru.bolshakov.internship.dishes_rating.controller.restaurant;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.bolshakov.internship.dishes_rating.dto.restaurant.RestaurantSavingRequestDTO;
import ru.bolshakov.internship.dishes_rating.service.RestaurantService;

import javax.validation.Valid;

@RestController
@RequestMapping("/restaurants")
public class RestaurantManagementController {

    private final RestaurantService service;

    public RestaurantManagementController(RestaurantService service) {
        this.service = service;
    }

    @ApiOperation(value = "Add new restaurant")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Creation was successful. Return ID which assigned to the created Restaurant"),
            @ApiResponse(code = 400, message = "Restaurant contains invalid parameters")})
    @PostMapping
    public ResponseEntity<Long> create(@RequestBody @Valid RestaurantSavingRequestDTO restaurant) {
        return ResponseEntity.ok(service.create(restaurant).getId());
    }

    @ApiOperation(value = "Update existent restaurant")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Updating was successful. Return ID of updated restaurant"),
            @ApiResponse(code = 400, message = "Restaurant contains invalid parameters"),
            @ApiResponse(code = 404, message = "Restaurant with such ID not found")})
    @PutMapping("/{restaurantId}")
    public ResponseEntity<Long> update(@PathVariable @Parameter(in = ParameterIn.PATH, description = "ID of restaurant") Long restaurantId,
                                       @RequestBody @Valid RestaurantSavingRequestDTO restaurant) {
        return ResponseEntity.ok(service.update(restaurant, restaurantId).getId());
    }

    @ApiOperation(value = "Delete existent restaurant")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Deleting was successful"),
            @ApiResponse(code = 404, message = "Restaurant with such ID not found")})
    @DeleteMapping("/{restaurantId}")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@PathVariable @Parameter(in = ParameterIn.PATH, description = "ID of restaurant") Long restaurantId) {
        service.delete(restaurantId);
    }
}
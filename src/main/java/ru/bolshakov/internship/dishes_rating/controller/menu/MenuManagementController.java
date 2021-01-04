package ru.bolshakov.internship.dishes_rating.controller.menu;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.bolshakov.internship.dishes_rating.dto.dish.DishSavingRequestDTO;
import ru.bolshakov.internship.dishes_rating.service.MenuService;

import javax.validation.Valid;

@RestController
@RequestMapping("/restaurants/{restaurantId}/menu/dishes")
public class MenuManagementController {

    private final MenuService dishService;

    public MenuManagementController(MenuService dishService) {
        this.dishService = dishService;
    }


    @ApiOperation(value = "Add new dish to the menu of this restaurant")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Creation was successful. Return ID which assigned to the created dish"),
            @ApiResponse(code = 404, message = "Restaurant with such ID not found"),
            @ApiResponse(code = 400, message = "Dish contains invalid parameters")})
    @PostMapping
    public ResponseEntity<Long> addDish(@PathVariable @Parameter(in = ParameterIn.PATH, description = "ID of existing restaurant") Long restaurantId,
                                        @RequestBody @Valid DishSavingRequestDTO request) {
        return ResponseEntity.ok(dishService.addDish(request, restaurantId).getId());
    }

    @ApiOperation(value = "Update existing dish in the menu of this restaurant")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Updating was successful. Return ID of updated dish"),
            @ApiResponse(code = 404, message = "Restaurant/Dish with such ID not found"),
            @ApiResponse(code = 400, message = "Dish contains invalid parameters")})
    @PutMapping("/{dishId}")
    public ResponseEntity<Long> updateDish(@PathVariable @Parameter(in = ParameterIn.PATH, description = "ID of existing restaurant") Long restaurantId,
                                           @PathVariable @Parameter(in = ParameterIn.PATH, description = "ID of existing dish which we want to update") Long dishId,
                                           @RequestBody @Valid DishSavingRequestDTO request) {
        return ResponseEntity.ok(dishService.updateDish(request, restaurantId, dishId).getId());
    }

    @ApiOperation(value = "Delete existing dish from the menu of this restaurant")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Deleting was successful"),
            @ApiResponse(code = 404, message = "Dish with such ID not found")})
    @DeleteMapping("/{dishId}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteDish(@PathVariable @Parameter(in = ParameterIn.PATH, description = "ID of existing restaurant") Long restaurantId,
                           @PathVariable @Parameter(in = ParameterIn.PATH, description = "ID of existing dish which we want to update") Long dishId) {
        dishService.deleteDish(restaurantId, dishId);
    }
}

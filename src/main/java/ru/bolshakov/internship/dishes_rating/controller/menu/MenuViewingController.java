package ru.bolshakov.internship.dishes_rating.controller.menu;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.bolshakov.internship.dishes_rating.dto.dish.DishDTO;
import ru.bolshakov.internship.dishes_rating.dto.menu.MenuDTO;
import ru.bolshakov.internship.dishes_rating.service.MenuService;

@RestController
@RequestMapping("/restaurants/{restaurantId}/menu")
public class MenuViewingController {

    private final MenuService service;

    public MenuViewingController(MenuService service) {
        this.service = service;
    }

    @ApiOperation(value = "Get menu with all dishes by restaurant (ID of restaurant defined on URL path) and current date")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Return all dishes from current menu"),
            @ApiResponse(code = 404, message = "Restaurant with such id not found")})
    @GetMapping
    public ResponseEntity<MenuDTO> getMenu(@PathVariable @Parameter(in = ParameterIn.PATH, name = "restaurantId", description = "ID of restaurant") Long restaurantId) {
        MenuDTO menu = service.getMenuByRestaurantId(restaurantId);
        return ResponseEntity.ok(menu);
    }

    @ApiOperation(value = "Get dish from menu by dishID")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Return dish from current menu by dishID"),
            @ApiResponse(code = 404, message = "Restaurant/Dish with such id not found")})
    @GetMapping("/dishes/{dishId}")
    public ResponseEntity<DishDTO> get(@PathVariable @Parameter(in = ParameterIn.PATH, name = "restaurantId", description = "ID of restaurant") Long restaurantId,
                                       @PathVariable @Parameter(in = ParameterIn.PATH, name = "dishId", description = "ID of dish") Long dishId) {
        DishDTO dish = service.getDish(restaurantId, dishId);
        return ResponseEntity.ok(dish);
    }
}

package ru.bolshakov.internship.dishes_rating.service.mapper;

import org.junit.jupiter.api.Test;
import ru.bolshakov.internship.dishes_rating.dto.dish.DishDTO;
import ru.bolshakov.internship.dishes_rating.model.jpa.Dish;
import ru.bolshakov.internship.dishes_rating.model.jpa.Menu;
import ru.bolshakov.internship.dishes_rating.model.jpa.Restaurant;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DishMapperTest {

    private final DishMapper mapper = new DishMapper();

    @Test
    void toEntity() {
        Restaurant restaurant = new Restaurant(123L, "restaurant1", "description1");
        Menu menu = new Menu(50L, LocalDate.now(), restaurant);
        DishDTO dishDTO = new DishDTO(123L, "dish1", 10005L, menu.getId());

        Dish dish = mapper.toEntity(dishDTO, menu);
        assertEquals(dishDTO.getName(), dish.getName());
        assertEquals(dishDTO.getPrice(), dish.getPrice());
        assertEquals(dishDTO.getMenuId(), dish.getMenu().getId());
    }

    @Test
    void toDTO() {
        Restaurant restaurant = new Restaurant(123L, "restaurant1", "description1");
        Menu menu = new Menu(50L, LocalDate.now(), restaurant);
        Dish dish = new Dish(123L, "dish1", 10005L, menu);

        DishDTO dishDTO = mapper.toDTO(dish);
        assertEquals(dish.getName(), dishDTO.getName());
        assertEquals(dish.getPrice(), dishDTO.getPrice());
        assertEquals(dish.getMenu().getId(), dishDTO.getMenuId());
        assertEquals("100.05", dishDTO.getFormattedPrice());
    }

    @Test
    void toDTOs() {
        Restaurant restaurant = new Restaurant(123L, "restaurant1", "description1");
        Menu menu = new Menu(50L, LocalDate.now(), restaurant);

        List<Dish> dishes = Arrays.asList(
                new Dish(123L, "name1", 10000L, menu),
                new Dish(233L, "name2", 15000L, menu),
                new Dish(321L, "name3", 10050L, menu));

        List<DishDTO> dishesDTO = mapper.toDTOs(dishes);

        assertEquals(dishes.get(0).getName(), dishesDTO.get(0).getName());
        assertEquals(dishes.get(0).getPrice(), dishesDTO.get(0).getPrice());
        assertEquals(dishes.get(0).getMenu().getId(), dishesDTO.get(0).getMenuId());

        assertEquals(dishes.get(1).getName(), dishesDTO.get(1).getName());
        assertEquals(dishes.get(1).getPrice(), dishesDTO.get(1).getPrice());
        assertEquals(dishes.get(1).getMenu().getId(), dishesDTO.get(1).getMenuId());

        assertEquals(dishes.get(2).getName(), dishesDTO.get(2).getName());
        assertEquals(dishes.get(2).getPrice(), dishesDTO.get(2).getPrice());
        assertEquals(dishes.get(2).getMenu().getId(), dishesDTO.get(2).getMenuId());
    }
}
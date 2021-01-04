package ru.bolshakov.internship.dishes_rating.service.mapper;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.bolshakov.internship.dishes_rating.dto.dish.DishDTO;
import ru.bolshakov.internship.dishes_rating.dto.menu.MenuDTO;
import ru.bolshakov.internship.dishes_rating.model.jpa.Dish;
import ru.bolshakov.internship.dishes_rating.model.jpa.Menu;
import ru.bolshakov.internship.dishes_rating.model.jpa.Restaurant;
import ru.bolshakov.internship.dishes_rating.service.PriceFormatter;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

class MenuMapperTest {

    MenuMapper mapper = new MenuMapper(new DishMapper());

    @Test
    void toDTO() {
        Restaurant restaurant = new Restaurant(123L, "restaurant", "description");
        Menu menu = new Menu(1L, LocalDate.now(), restaurant);


        Set<Dish> dishSet = new HashSet<>(Arrays.asList(
                new Dish(1L, "name1", 10000L, menu),
                new Dish(2L, "name2", 15000L, menu),
                new Dish(3L, "name3", 10050L, menu)));
        menu.setDishes(dishSet);

        List<DishDTO> dishDTOList = Arrays.asList(
                new DishDTO(1L, "name1", 10000L, menu.getId(), "100.00"),
                new DishDTO(2L, "name2", 15000L, menu.getId(), "150.00"),
                new DishDTO(3L, "name3", 10050L, menu.getId(), "100.50"));

        Long averagePrice = Math.round(dishDTOList.stream().mapToDouble(DishDTO::getPrice).average().orElse(0));
        String formattedAveragePrice = PriceFormatter.doPriceFormatting(averagePrice);
        MenuDTO expectedMenuDTO = new MenuDTO(menu.getMenuDate(), menu.getRestaurant().getId(), averagePrice, formattedAveragePrice, dishDTOList);
        MenuDTO returnedMenuDTO = mapper.toDTO(menu, averagePrice);

        Assertions.assertEquals(expectedMenuDTO.getMenuDate(), returnedMenuDTO.getMenuDate());
        Assertions.assertEquals(expectedMenuDTO.getRestaurantId(), returnedMenuDTO.getRestaurantId());
        Assertions.assertEquals(expectedMenuDTO.getAveragePrice(), returnedMenuDTO.getAveragePrice());
        Assertions.assertEquals(expectedMenuDTO.getFormattedAveragePrice(), returnedMenuDTO.getFormattedAveragePrice());
        Assertions.assertEquals(expectedMenuDTO.getDishes(), returnedMenuDTO.getDishes());
    }
}
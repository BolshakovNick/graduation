package ru.bolshakov.internship.dishes_rating.service.mapper;

import org.junit.jupiter.api.Test;
import ru.bolshakov.internship.dishes_rating.dto.restaurant.RestaurantDTO;
import ru.bolshakov.internship.dishes_rating.model.jpa.Restaurant;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RestaurantMapperTest {

    private final RestaurantMapper mapper = new RestaurantMapper();

    @Test
    void toEntity() {
        RestaurantDTO restaurantDTO = new RestaurantDTO(1L,"name", "description");

        Restaurant restaurant = mapper.toEntity(restaurantDTO);

        assertEquals(restaurantDTO.getId(), restaurant.getId());
        assertEquals(restaurantDTO.getRestaurantName(), restaurant.getRestaurantName());
        assertEquals(restaurantDTO.getDescription(), restaurant.getDescription());
    }

    @Test
    void toDTO() {
        Restaurant restaurant = new Restaurant(1L,"name", "description");

        RestaurantDTO restaurantDTO = mapper.toDTO(restaurant);

        assertEquals(restaurant.getId(), restaurantDTO.getId());
        assertEquals(restaurant.getRestaurantName(), restaurantDTO.getRestaurantName());
        assertEquals(restaurant.getDescription(), restaurantDTO.getDescription());
    }

    @Test
    void toDTOs() {
        List<Restaurant> restaurants = Arrays.asList(
                new Restaurant(1L,"name1", "description1"),
                new Restaurant(2L,"name2", "description2"),
                new Restaurant(3L,"name3", "description3"));

        List<RestaurantDTO> restaurantsDTO = mapper.toDTOs(restaurants);

        assertEquals(restaurants.get(0).getId(), restaurantsDTO.get(0).getId());
        assertEquals(restaurants.get(0).getRestaurantName(), restaurantsDTO.get(0).getRestaurantName());
        assertEquals(restaurants.get(0).getDescription(), restaurantsDTO.get(0).getDescription());

        assertEquals(restaurants.get(1).getId(), restaurantsDTO.get(1).getId());
        assertEquals(restaurants.get(1).getRestaurantName(), restaurantsDTO.get(1).getRestaurantName());
        assertEquals(restaurants.get(1).getDescription(), restaurantsDTO.get(1).getDescription());

        assertEquals(restaurants.get(2).getId(), restaurantsDTO.get(2).getId());
        assertEquals(restaurants.get(2).getRestaurantName(), restaurantsDTO.get(2).getRestaurantName());
        assertEquals(restaurants.get(2).getDescription(), restaurantsDTO.get(2).getDescription());
    }
}
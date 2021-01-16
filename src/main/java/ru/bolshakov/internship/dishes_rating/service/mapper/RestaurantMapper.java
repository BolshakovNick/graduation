package ru.bolshakov.internship.dishes_rating.service.mapper;

import org.springframework.stereotype.Component;
import ru.bolshakov.internship.dishes_rating.dto.restaurant.RestaurantDTO;
import ru.bolshakov.internship.dishes_rating.dto.restaurant.RestaurantSavingRequestDTO;
import ru.bolshakov.internship.dishes_rating.model.Restaurant;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class RestaurantMapper {

    public Restaurant toEntity(RestaurantDTO restaurantDTO) {
        return new Restaurant(restaurantDTO.getId(), restaurantDTO.getRestaurantName(), restaurantDTO.getDescription());
    }

    public Restaurant toEntity(RestaurantSavingRequestDTO restaurantDTO) {
        return new Restaurant(null, restaurantDTO.getRestaurantName(), restaurantDTO.getDescription());
    }

    public Restaurant toEntity(RestaurantSavingRequestDTO restaurantDTO, Long restaurantId) {
        return new Restaurant(restaurantId, restaurantDTO.getRestaurantName(), restaurantDTO.getDescription());
    }

    public Restaurant toEntity(Restaurant restaurant, RestaurantSavingRequestDTO dto) {
        restaurant.setRestaurantName(dto.getRestaurantName());
        restaurant.setDescription(dto.getDescription());
        return restaurant;
    }

    public RestaurantDTO toDTO(Restaurant restaurant, Long rating) {
        return new RestaurantDTO(restaurant.getId(), restaurant.getRestaurantName(), restaurant.getDescription(), rating);
    }

    public RestaurantDTO toDTO(Restaurant restaurant) {
        return new RestaurantDTO(restaurant.getId(), restaurant.getRestaurantName(), restaurant.getDescription());
    }

    public List<RestaurantDTO> toDTOs(List<Restaurant> restaurants, Map<Long, Long> ratingByRestaurant) {
        return restaurants.stream().map(restaurant -> toDTO(restaurant, ratingByRestaurant.get(restaurant.getId()))).collect(Collectors.toList());
    }

    public List<RestaurantDTO> toDTOs(List<Restaurant> restaurants) {
        return restaurants.stream().map(restaurant -> toDTO(restaurant, null)).collect(Collectors.toList());
    }
}

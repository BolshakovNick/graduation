package ru.bolshakov.internship.dishes_rating.service.mapper;

import org.springframework.stereotype.Component;
import ru.bolshakov.internship.dishes_rating.dto.dish.DishDTO;
import ru.bolshakov.internship.dishes_rating.dto.dish.DishSavingRequestDTO;
import ru.bolshakov.internship.dishes_rating.model.Dish;
import ru.bolshakov.internship.dishes_rating.model.Menu;
import ru.bolshakov.internship.dishes_rating.service.PriceFormatter;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class DishMapper {

    public Dish toEntity(DishSavingRequestDTO dto, Menu menu) {
        return toEntity(dto, menu, null);
    }

    public Dish toEntity(DishSavingRequestDTO dto, Menu menu, Long dishId) {
        return new Dish(dishId, dto.getName(), dto.getPrice(), menu);
    }

    public Dish toEntity(DishDTO dishDTO, Menu menu) {
        return new Dish(dishDTO.getId(), dishDTO.getName(), dishDTO.getPrice(), menu);
    }

    public DishDTO toDTO(Dish dish) {
        return new DishDTO(
                dish.getId(),
                dish.getName(),
                dish.getPrice(),
                dish.getMenu().getId(),
                PriceFormatter.doPriceFormatting(dish.getPrice())
        );
    }

    public List<DishDTO> toDTOs(List<Dish> dishes) {
        return dishes.stream().map(this::toDTO).collect(Collectors.toList());
    }
}

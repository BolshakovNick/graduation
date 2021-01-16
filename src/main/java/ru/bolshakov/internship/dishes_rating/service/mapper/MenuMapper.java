package ru.bolshakov.internship.dishes_rating.service.mapper;

import org.springframework.stereotype.Component;
import ru.bolshakov.internship.dishes_rating.dto.menu.MenuDTO;
import ru.bolshakov.internship.dishes_rating.model.Menu;
import ru.bolshakov.internship.dishes_rating.service.PriceFormatter;

import java.util.stream.Collectors;

@Component
public class MenuMapper {
    private final DishMapper dishMapper;

    public MenuMapper(DishMapper dishMapper) {
        this.dishMapper = dishMapper;
    }

    public MenuDTO toDTO(Menu menu) {
        return toDTO(menu, null);
    }

    public MenuDTO toDTO(Menu menu, Long averagePrice) {
        return new MenuDTO(
                menu.getMenuDate(),
                menu.getRestaurant().getId(),
                averagePrice,
                averagePrice == null ? null : PriceFormatter.doPriceFormatting(averagePrice),
                menu.getDishes() == null ? null
                        : menu.getDishes().stream().map(dishMapper::toDTO).collect(Collectors.toList())
        );
    }
}

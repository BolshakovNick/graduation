package ru.bolshakov.internship.dishes_rating.data.jpa;

import ru.bolshakov.internship.dishes_rating.model.jpa.Dish;
import ru.bolshakov.internship.dishes_rating.model.jpa.Menu;
import ru.bolshakov.internship.dishes_rating.model.jpa.Restaurant;

import java.time.LocalDate;

public class TestDishData {
    private final Menu menu = new Menu(1L, LocalDate.now(), new Restaurant(123L, "restaurant", "description"));
    public final Dish TEST_DISH_1 = new Dish("Dish1", 100000L, menu);
    public final Dish TEST_DISH_2 = new Dish("Dish2", 150000L, menu);
    public final Dish TEST_DISH_3 = new Dish("Dish3", 80000L, menu);

    public Long existentDishId;
    public final Long nonExistentDishId = 0L;
}

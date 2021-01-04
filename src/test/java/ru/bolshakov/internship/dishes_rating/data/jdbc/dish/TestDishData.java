package ru.bolshakov.internship.dishes_rating.data.jdbc.dish;

import ru.bolshakov.internship.dishes_rating.model.jdbc.Dish;

public class TestDishData {
    public final Dish TEST_DISH_1 = new Dish("Dish1", 100000L, 1L);
    public final Dish TEST_DISH_2 = new Dish("Dish2", 150000L, 1L);
    public final Dish TEST_DISH_3 = new Dish("Dish3", 80000L, 1L);

    public Long existentDishId;
    public final Long nonExistentDishId = 0L;
}

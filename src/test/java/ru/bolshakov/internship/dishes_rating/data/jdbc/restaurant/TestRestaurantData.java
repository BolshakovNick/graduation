package ru.bolshakov.internship.dishes_rating.data.jdbc.restaurant;

import ru.bolshakov.internship.dishes_rating.model.jdbc.Restaurant;

public class TestRestaurantData {
    public final Restaurant TEST_RESTAURANT_1 = new Restaurant("Restaurant1", "Some description for first restaurant");
    public final Restaurant TEST_RESTAURANT_2 = new Restaurant("Restaurant2", "Some description for second restaurant");
    public final Restaurant TEST_RESTAURANT_3 = new Restaurant("Restaurant3", "Some description for third restaurant");

    public Long existentRestaurantId;
    public final Long nonExistentRestaurantId = 0L;
}

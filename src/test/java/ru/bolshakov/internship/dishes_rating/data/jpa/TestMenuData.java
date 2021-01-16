package ru.bolshakov.internship.dishes_rating.data.jpa;

import ru.bolshakov.internship.dishes_rating.model.Menu;
import ru.bolshakov.internship.dishes_rating.model.Restaurant;

import java.time.LocalDate;

public class TestMenuData {
    private final Restaurant restaurant1 = new Restaurant(1L, "restaurant1", "description1");
    private final Restaurant restaurant2 = new Restaurant(2L, "restaurant2", "description2");
    private final Restaurant restaurant3 = new Restaurant(3L, "restaurant3", "description3");

    public final Menu TEST_MENU_1 = new Menu(LocalDate.now(), restaurant1);
    public final Menu TEST_MENU_2 = new Menu(LocalDate.now(), restaurant2);
    public final Menu TEST_MENU_3 = new Menu(LocalDate.now(), restaurant3);

    public final Restaurant restaurantWithoutMenu = new Restaurant(4L, "restaurant4 name", "description for restaurant4");
    public final Restaurant restaurantWithMenu = new Restaurant(1L, "restaurant1 name", "description for restaurant1");
    public final Restaurant nonExistentRestaurant = new Restaurant(0L, "restaurant0 name", "description for restaurant0");;
    public final LocalDate notToday = LocalDate.of(2020, 11, 10);
    public final LocalDate today = LocalDate.now();

    public Long existentMenuId;
    public final Long nonExistentMenuId = 0L;
}

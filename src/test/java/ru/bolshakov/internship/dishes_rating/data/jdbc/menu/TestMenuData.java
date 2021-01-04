package ru.bolshakov.internship.dishes_rating.data.jdbc.menu;

import ru.bolshakov.internship.dishes_rating.model.jdbc.Menu;

import java.time.LocalDate;


public class TestMenuData {
    public final Menu TEST_MENU_1 = new Menu(LocalDate.now(), 1L);
    public final Menu TEST_MENU_2 = new Menu(LocalDate.now(), 2L);
    public final Menu TEST_MENU_3 = new Menu(LocalDate.now(), 3L);

    public final Long restaurantWithoutMenuId = 4L;
    public final Long restaurantWithMenuId = 1L;
    public final Long nonExistentRestaurantId = 0L;
    public final LocalDate notToday = LocalDate.of(2020, 11, 10);
    public final LocalDate today = LocalDate.now();

    public Long existentMenuId;
    public final Long nonExistentMenuId = 0L;
}

package ru.bolshakov.internship.dishes_rating.model.jdbc;

import java.time.LocalDate;

public class Menu extends BaseEntity {
    private final LocalDate menuDate;

    private final Long restaurantId;

    public Menu(LocalDate menuDate, Long restaurantId) {
        this(null, menuDate, restaurantId);
    }

    public Menu(Long id, LocalDate menuDate, Long restaurantId) {
        super(id);
        this.menuDate = menuDate;
        this.restaurantId = restaurantId;
    }

    public LocalDate getMenuDate() {
        return menuDate;
    }

    public Long getRestaurantId() {
        return restaurantId;
    }

    @Override
    public String toString() {
        return "Menu{" +
                "id=" + super.id +
                "menu_datetime=" + menuDate +
                ", restaurantId=" + restaurantId +
                '}';
    }
}

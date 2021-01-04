package ru.bolshakov.internship.dishes_rating.dto.search;

import java.util.Objects;

public class RestaurantSearchRequest {

    private String restaurantName;

    private boolean nameStartWith;

    private boolean nameEndWith;

    private String description;

    private boolean descStartWith;

    private boolean descEndWith;

    public RestaurantSearchRequest() {
    }

    public RestaurantSearchRequest(String restaurantName, boolean nameStartWith, boolean nameEndWith, String description, boolean descStartWith, boolean descEndWith) {
        this.restaurantName = restaurantName;
        this.nameStartWith = nameStartWith;
        this.nameEndWith = nameEndWith;
        this.description = description;
        this.descStartWith = descStartWith;
        this.descEndWith = descEndWith;
}

    public String getRestaurantName() {
        return restaurantName;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }

    public boolean isNameStartWith() {
        return nameStartWith;
    }

    public void setNameStartWith(boolean nameStartWith) {
        this.nameStartWith = nameStartWith;
    }

    public boolean isNameEndWith() {
        return nameEndWith;
    }

    public void setNameEndWith(boolean nameEndWith) {
        this.nameEndWith = nameEndWith;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isDescStartWith() {
        return descStartWith;
    }

    public void setDescStartWith(boolean descStartWith) {
        this.descStartWith = descStartWith;
    }

    public boolean isDescEndWith() {
        return descEndWith;
    }

    public void setDescEndWith(boolean descEndWith) {
        this.descEndWith = descEndWith;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RestaurantSearchRequest that = (RestaurantSearchRequest) o;
        return Objects.equals(restaurantName, that.restaurantName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(restaurantName);
    }
}

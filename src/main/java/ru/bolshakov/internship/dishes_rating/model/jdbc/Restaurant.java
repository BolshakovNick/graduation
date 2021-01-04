package ru.bolshakov.internship.dishes_rating.model.jdbc;

public class Restaurant extends BaseEntity {
    private String restaurantName;

    private String description;

    public Restaurant(String restaurantName) {
        this(restaurantName, null);
    }

    public Restaurant(String restaurantName, String description) {
        this(null, restaurantName, description);
    }

    public Restaurant(Long id, String restaurantName, String description) {
        super(id);
        this.restaurantName = restaurantName;
        this.description = description;
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "Restaurant{" +
                "id=" + super.id +
                "restaurantName=" + restaurantName +
                ", description=" + description +
                '}';
    }
}

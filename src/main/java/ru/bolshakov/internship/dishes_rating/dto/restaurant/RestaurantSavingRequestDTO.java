package ru.bolshakov.internship.dishes_rating.dto.restaurant;

import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Objects;

public class RestaurantSavingRequestDTO {
    @Schema(description = "Name of this restaurant", example = "restaurant")
    @Size(min = 5, max = 50)
    @NotBlank
    private String restaurantName;

    @Schema(description = "Description of this restaurant", example = "Some text which describes this restaurant")
    private String description;

    public RestaurantSavingRequestDTO(String restaurantName, String description) {
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RestaurantSavingRequestDTO that = (RestaurantSavingRequestDTO) o;
        return restaurantName.equals(that.restaurantName) &&
                Objects.equals(description, that.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(restaurantName, description);
    }

    @Override
    public String toString() {
        return "RestaurantSavingRequestDTO{" +
                "restaurantName='" + restaurantName + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}

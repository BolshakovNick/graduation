package ru.bolshakov.internship.dishes_rating.dto.restaurant;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class RestaurantDTO {

    @Schema(description = "ID of this restaurant", example = "1")
    private Long id;

    @Schema(description = "Name of this restaurant", example = "restaurant")
    @Size(min = 5, max = 50)
    @NotBlank
    private String restaurantName;

    @Schema(description = "Description of this restaurant", example = "Some text which describes this restaurant")
    private String description;

    @Schema(description = "Rating which computes by votes of Users", example = "100")
    private Long rating;

    public RestaurantDTO(String restaurantName, String description) {
        this(null, restaurantName, description, null);
    }

    public RestaurantDTO(String restaurantName, String description, Long rating) {
        this(null, restaurantName, description, rating);
    }

    public RestaurantDTO(Long id, String restaurantName, String description) {
        this(id, restaurantName, description, null);
    }

    public RestaurantDTO(Long id, String restaurantName, String description, Long rating) {
        this.id = id;
        this.restaurantName = restaurantName;
        this.description = description;
        this.rating = rating;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Long getRating() {
        return rating;
    }

    public void setRating(Long rating) {
        this.rating = rating;
    }

    @Override
    public String toString() {
        return "RestaurantDTO{" +
                "id=" + id +
                ", restaurantName='" + restaurantName + '\'' +
                ", description='" + description + '\'' +
                ", rating=" + rating +
                '}';
    }
}

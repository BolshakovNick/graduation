package ru.bolshakov.internship.dishes_rating.dto.vote;

import io.swagger.v3.oas.annotations.media.Schema;
import ru.bolshakov.internship.dishes_rating.dto.restaurant.RestaurantDTO;
import ru.bolshakov.internship.dishes_rating.dto.user.UserDTO;

import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalTime;

public class VoteDTO {

    @Schema(description = "ID of this vote", example = "1")
    @PositiveOrZero
    private Long id;

    @Schema(description = "Time which shows when user vote")
    @PastOrPresent
    private LocalTime localTime;

    @Schema(description = "ID of user who votes", example = "1")
    private UserDTO user;

    @Schema(description = "ID of restaurant the user voted for", example = "1")
    private RestaurantDTO restaurant;

    public VoteDTO(UserDTO user, LocalTime localTime, RestaurantDTO restaurant) {
        this(null, user, localTime, restaurant);
    }

    public VoteDTO(Long id, UserDTO user, LocalTime localTime, RestaurantDTO restaurant) {
        this.id = id;
        this.user = user;
        this.localTime = localTime;
        this.restaurant = restaurant;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalTime getLocalTime() {
        return localTime;
    }

    public void setLocalTime(LocalTime localTime) {
        this.localTime = localTime;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    public RestaurantDTO getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(RestaurantDTO restaurant) {
        this.restaurant = restaurant;
    }

    @Override
    public String toString() {
        return "VoteDTO{" +
                "id=" + id +
                ", localTime=" + localTime +
                ", userId=" + user +
                ", restaurantId=" + restaurant +
                '}';
    }
}

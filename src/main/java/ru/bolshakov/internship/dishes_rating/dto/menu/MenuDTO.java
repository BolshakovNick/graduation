package ru.bolshakov.internship.dishes_rating.dto.menu;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import ru.bolshakov.internship.dishes_rating.dto.dish.DishDTO;

import java.time.LocalDate;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class MenuDTO {
    @Schema(description = "The date this menu was created")
    private LocalDate menuDate;

    @Schema(description = "ID of restaurant that stores this menu", example = "1")
    private Long restaurantId;

    @Schema(description = "Average price for current menu stored in the minimal unit (e.g. penny/kopecks)")
    private Long averagePrice;

    @Schema(description = "Average price for current menu in human-friendly format (e.g. rubble.kopecks)")
    private String formattedAveragePrice;

    @Schema(description = "List of dishes in this menu")
    private List<DishDTO> dishes;

    public MenuDTO(LocalDate menuDate, Long restaurantId, Long averagePrice, String formattedAveragePrice, List<DishDTO> dishes) {
        this.menuDate = menuDate;
        this.restaurantId = restaurantId;
        this.averagePrice = averagePrice;
        this.formattedAveragePrice = formattedAveragePrice;
        this.dishes = dishes;
    }

    public LocalDate getMenuDate() {
        return menuDate;
    }

    public void setMenuDate(LocalDate menuDate) {
        this.menuDate = menuDate;
    }

    public Long getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(Long restaurantId) {
        this.restaurantId = restaurantId;
    }

    public List<DishDTO> getDishes() {
        return dishes;
    }

    public void setDishes(List<DishDTO> dishes) {
        this.dishes = dishes;
    }

    public Long getAveragePrice() {
        return averagePrice;
    }

    public void setAveragePrice(Long averagePrice) {
        this.averagePrice = averagePrice;
    }

    public String getFormattedAveragePrice() {
        return formattedAveragePrice;
    }

    public void setFormattedAveragePrice(String formattedAveragePrice) {
        this.formattedAveragePrice = formattedAveragePrice;
    }

    @Override
    public String toString() {
        return "MenuDTO{" +
                "menuDate=" + menuDate +
                ", restaurantId=" + restaurantId +
                ", dishes=" + dishes +
                ", averagePrice='" + formattedAveragePrice + '\'' +
                '}';
    }
}

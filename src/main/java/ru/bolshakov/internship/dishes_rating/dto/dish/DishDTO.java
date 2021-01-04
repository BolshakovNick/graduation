package ru.bolshakov.internship.dishes_rating.dto.dish;

import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.*;
import java.util.Objects;

public class DishDTO {

    @Schema(description = "ID of this dish", example = "1")
    private Long id;

    @Schema(description = "Name of the dish", example = "dish")
    @NotBlank
    @Size(max = 50)
    private String name;

    @Schema(description = "Price of the dish, stored in the minimal unit (e.g. penny/kopecks)", example = "10090")
    @Positive
    private Long price;

    @Schema(description = "Price of the dish, presented in human-friendly format (e.g. rubble.kopecks)", example = "100.90")
    private String formattedPrice;

    @Schema(description = "ID of menu in which this dish is stored", example = "1")
    @NotNull
    @PositiveOrZero
    private Long menuId;

    public DishDTO(String name, Long price, Long menuId) {
        this(null, name, price, menuId);
    }

    public DishDTO(Long id, String name, Long price, Long menuId) {
        this(id, name, price, menuId, String.valueOf(price));
    }

    public DishDTO(Long id, String name, Long price, Long menuId, String formattedPrice) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuId = menuId;
        this.formattedPrice = formattedPrice;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    public Long getMenuId() {
        return menuId;
    }

    public void setMenuId(Long menuId) {
        this.menuId = menuId;
    }

    public void setFormattedPrice(String formattedPrice) {
        this.formattedPrice = formattedPrice;
    }

    public String getFormattedPrice() {
        return formattedPrice;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DishDTO dishDTO = (DishDTO) o;
        return Objects.equals(id, dishDTO.id) &&
                name.equals(dishDTO.name) &&
                price.equals(dishDTO.price) &&
                Objects.equals(formattedPrice, dishDTO.formattedPrice) &&
                menuId.equals(dishDTO.menuId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, price, formattedPrice, menuId);
    }

    @Override
    public String toString() {
        return "DishDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", formattedPrice='" + formattedPrice + '\'' +
                ", menuId=" + menuId +
                '}';
    }
}

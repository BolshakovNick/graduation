package ru.bolshakov.internship.dishes_rating.dto.dish;

import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.util.Objects;

public class DishSavingRequestDTO {

    @Schema(description = "Name of the dish", example = "dish")
    @NotBlank
    @Size(max = 50)
    private String name;

    @Schema(description = "Price of the dish, stored in the minimal unit (e.g. penny/kopecks)", example = "10090")
    @Positive
    private Long price;

    public DishSavingRequestDTO(String name, Long price) {
        this.name = name;
        this.price = price;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DishSavingRequestDTO that = (DishSavingRequestDTO) o;
        return name.equals(that.name) &&
                price.equals(that.price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, price);
    }

    @Override
    public String toString() {
        return "DishSavingRequestDTO{" +
                "name='" + name + '\'' +
                ", price=" + price + '}';
    }
}

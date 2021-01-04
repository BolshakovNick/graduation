package ru.bolshakov.internship.dishes_rating.model.jdbc;

public class Dish extends BaseEntity {

    private final Long menuId;

    private String name;

    private Long price;

    public Dish(String name, Long price, Long menuId) {
        this(null, name, price, menuId);
    }

    public Dish(Long id, String name, Long price, Long menuId) {
        super(id);
        this.name = name;
        this.price = price;
        this.menuId = menuId;
    }

    public Long getMenuId() {
        return menuId;
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
    public String toString() {
        return "Dish{" +
                "id=" + super.id +
                "menuId=" + menuId +
                ", dishName='" + name + '\'' +
                ", price=" + price +
                '}';
    }
}

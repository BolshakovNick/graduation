package ru.bolshakov.internship.dishes_rating.model;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "dish")
@NamedEntityGraph(name = "Dish.menu", attributeNodes = @NamedAttributeNode("menu"))
public class Dish {

    @Id
    @SequenceGenerator(name = "dish_id_seq", sequenceName = "dish_id_seq", allocationSize = 1, initialValue = 100000)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "dish_id_seq")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="menu_id")
    private Menu menu;

    @Column(name = "dish_name", nullable = false)
    private String name;

    @Column(name = "price", nullable = false)
    private Long price;

    public Dish() {
    }

    public Dish(String name, Long price, Menu menu) {
        this(null, name, price, menu);
    }

    public Dish(Long id, String name, Long price, Menu menu) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.menu = menu;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Menu getMenu() {
        return menu;
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
        Dish dish = (Dish) o;
        return Objects.equals(id, dish.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Dish{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                '}';
    }

    public boolean isNew() {
        return id == null;
    }
}

package ru.bolshakov.internship.dishes_rating.repository.jpa;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.bolshakov.internship.dishes_rating.model.jpa.Dish;

import java.util.List;
import java.util.Optional;

@Repository
public interface JpaDishRepository extends JpaRepository<Dish, Long> {

    List<Dish> findAllByMenu_Id(Long menuId);

    @EntityGraph(value = "Dish.menu")
    Optional<Dish> getById(Long dishId);
}

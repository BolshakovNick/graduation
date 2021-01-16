package ru.bolshakov.internship.dishes_rating.repository.jpa;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.bolshakov.internship.dishes_rating.model.Menu;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface JpaMenuRepository extends JpaRepository<Menu, Long> {

    List<Menu> findByRestaurant_Id(Long restaurantId);

    @EntityGraph(value = "Menu.dishes")
    Optional<Menu> findByRestaurant_IdAndMenuDate(Long restaurantId, LocalDate date);
}

package ru.bolshakov.internship.dishes_rating.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import ru.bolshakov.internship.dishes_rating.model.jpa.Restaurant;

import java.util.Optional;

@Repository
public interface JpaRestaurantRepository extends JpaRepository<Restaurant, Long>, JpaSpecificationExecutor<Restaurant> {

    Optional<Restaurant> findByRestaurantName(String name);
}

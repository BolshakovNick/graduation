package ru.bolshakov.internship.dishes_rating.service.specification.builder;

import org.springframework.data.jpa.domain.Specification;
import ru.bolshakov.internship.dishes_rating.dto.search.RestaurantSearchRequest;
import ru.bolshakov.internship.dishes_rating.model.jpa.Restaurant;
import ru.bolshakov.internship.dishes_rating.service.specification.RestaurantSpecification;

import java.util.Objects;

public class RestaurantSpecificationBuilder extends SpecificationBuilder{
    private static final String PARAMETER_NAME_DESCRIPTION = "description";
    private static final String PARAMETER_NAME_RESTAURANT_NAME = "restaurantName";

    public Specification<Restaurant> build(RestaurantSearchRequest restaurantSearchRequest) {
        if(restaurantSearchRequest.getRestaurantName() != null) {
            this.addCriteria(PARAMETER_NAME_RESTAURANT_NAME, restaurantSearchRequest.getRestaurantName(), restaurantSearchRequest.isNameStartWith(), restaurantSearchRequest.isNameEndWith());
        }
        if(restaurantSearchRequest.getDescription() != null) {
            this.addCriteria(PARAMETER_NAME_DESCRIPTION, restaurantSearchRequest.getDescription(), restaurantSearchRequest.isDescStartWith(), restaurantSearchRequest.isDescEndWith());
        }

        Specification<Restaurant> result = new RestaurantSpecification(searchCriteria.get(0));
        for (int i = 1; i < searchCriteria.size(); i++) {
            result = Objects.requireNonNull(Specification.where(result))
                    .and(new RestaurantSpecification(searchCriteria.get(i)));
        }
        return result;
    }
}

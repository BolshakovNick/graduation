package ru.bolshakov.internship.dishes_rating.service.specification.builder;

import org.springframework.data.jpa.domain.Specification;
import ru.bolshakov.internship.dishes_rating.dto.search.SearchRequest;
import ru.bolshakov.internship.dishes_rating.model.jpa.Restaurant;
import ru.bolshakov.internship.dishes_rating.service.specification.RestaurantSpecification;

public class RestaurantSpecificationBuilder extends SpecificationBuilder {

    public Specification<Restaurant> build(SearchRequest searchRequest) {
        this.setCriteria(searchRequest.getParameter(), searchRequest.getValue(), searchRequest.getOperator());
        return new RestaurantSpecification(searchCriteria);
    }
}

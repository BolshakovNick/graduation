package ru.bolshakov.internship.dishes_rating.service.specification.builder;

import org.springframework.data.jpa.domain.Specification;
import ru.bolshakov.internship.dishes_rating.dto.search.SearchRequest;
import ru.bolshakov.internship.dishes_rating.model.jpa.User;
import ru.bolshakov.internship.dishes_rating.service.specification.UserSpecification;

public class UserSpecificationBuilder extends SpecificationBuilder {

    public Specification<User> build(SearchRequest request) {
        this.setCriteria(request.getParameter(), request.getValue(), request.getOperator());
        return new UserSpecification(searchCriteria);
    }
}

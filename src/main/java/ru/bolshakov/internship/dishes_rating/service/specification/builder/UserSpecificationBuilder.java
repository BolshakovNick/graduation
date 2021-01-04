package ru.bolshakov.internship.dishes_rating.service.specification.builder;

import org.springframework.data.jpa.domain.Specification;
import ru.bolshakov.internship.dishes_rating.dto.search.UserSearchRequest;
import ru.bolshakov.internship.dishes_rating.model.jpa.User;
import ru.bolshakov.internship.dishes_rating.service.specification.UserSpecification;

public class UserSpecificationBuilder extends SpecificationBuilder {
    private static final String PARAMETER_NAME_USER_NAME = "userName";

    public Specification<User> build(UserSearchRequest request) {
        this.addCriteria(PARAMETER_NAME_USER_NAME, request.getUserName(), request.isStartWith(), request.isEndWith());
        return new UserSpecification(searchCriteria.get(0));
    }
}

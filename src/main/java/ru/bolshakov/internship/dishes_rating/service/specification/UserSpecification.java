package ru.bolshakov.internship.dishes_rating.service.specification;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.NonNull;
import ru.bolshakov.internship.dishes_rating.model.User;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class UserSpecification implements Specification<User> {

    private final SearchCriteria criteria;

    public UserSpecification(SearchCriteria criteria) {
        this.criteria = criteria;
    }

    @Override
    public Predicate toPredicate(@NonNull Root<User> root, @NonNull CriteriaQuery<?> query, @NonNull CriteriaBuilder builder) {
        return PredicateResolver.resolvePredicate(root, builder, criteria);
    }
}

package ru.bolshakov.internship.dishes_rating.service.specification.operation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.bolshakov.internship.dishes_rating.service.specification.PredicateResolver;
import ru.bolshakov.internship.dishes_rating.service.specification.SearchCriteria;
import ru.bolshakov.internship.dishes_rating.service.specification.SearchOperationType;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

@Component
public interface Operation {

    Predicate resolvePredicate(Root<?> root, CriteriaBuilder builder, SearchCriteria criteria);

    SearchOperationType getOperationType();

    @Autowired
    default void register(PredicateResolver resolver) {
        resolver.addOperation(getOperationType(), this);
    }
}

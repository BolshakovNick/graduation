package ru.bolshakov.internship.dishes_rating.service.specification.operation;

import org.springframework.stereotype.Component;
import ru.bolshakov.internship.dishes_rating.service.specification.SearchCriteria;
import ru.bolshakov.internship.dishes_rating.service.specification.SearchOperationType;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

@Component
public class EndsWithOperation implements Operation{

    @Override
    public Predicate resolvePredicate(Root<?> root, CriteriaBuilder builder, SearchCriteria criteria) {
        return builder.like(root.get(criteria.getKey()), "%" + criteria.getValue());
    }

    @Override
    public SearchOperationType getOperationType() {
        return SearchOperationType.ENDS;
    }
}

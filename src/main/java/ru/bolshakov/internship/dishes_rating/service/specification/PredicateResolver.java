package ru.bolshakov.internship.dishes_rating.service.specification;

import org.springframework.stereotype.Component;
import ru.bolshakov.internship.dishes_rating.exception.BadParameterException;
import ru.bolshakov.internship.dishes_rating.service.specification.operation.Operation;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.HashMap;
import java.util.Map;

@Component
public class PredicateResolver {
    private static final Map<SearchOperationType, Operation> operations = new HashMap<>();

    public static Predicate resolvePredicate(Root<?> root, CriteriaBuilder builder, SearchCriteria criteria) {
        try {
            Operation op = operations.get(criteria.getOperationType());
            return op.resolvePredicate(root, builder, criteria);
        } catch (IllegalArgumentException e) {
            throw new BadParameterException("'" + criteria.getKey() + "' is non-existent parameter.");
        }
    }

    public void addOperation(SearchOperationType type, Operation operation) {
        operations.put(type, operation);
    }
}

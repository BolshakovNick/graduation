package ru.bolshakov.internship.dishes_rating.service.specification.builder;

import ru.bolshakov.internship.dishes_rating.service.specification.SearchCriteria;
import ru.bolshakov.internship.dishes_rating.service.specification.SearchOperation;

import java.util.ArrayList;
import java.util.List;

public abstract class SpecificationBuilder {
    protected final List<SearchCriteria> searchCriteria = new ArrayList<>();

    protected void addCriteria(String key, String value, boolean startWith, boolean endWith) {
        SearchOperation searchOperation;
        if (startWith && endWith) {
            searchOperation = SearchOperation.CONTAINS;
        } else if (endWith) {
            searchOperation = SearchOperation.ENDS_WITH;
        } else if (startWith) {
            searchOperation = SearchOperation.STARTS_WITH;
        } else {
            searchOperation = SearchOperation.LIKE;
        }
        searchCriteria.add(new SearchCriteria(key, searchOperation, value));
    }
}

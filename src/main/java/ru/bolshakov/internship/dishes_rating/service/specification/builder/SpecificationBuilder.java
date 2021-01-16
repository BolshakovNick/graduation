package ru.bolshakov.internship.dishes_rating.service.specification.builder;

import ru.bolshakov.internship.dishes_rating.service.specification.SearchCriteria;
import ru.bolshakov.internship.dishes_rating.service.specification.SearchOperationType;

public abstract class SpecificationBuilder {
    protected SearchCriteria searchCriteria;

    protected void setCriteria(String key, String value, SearchOperationType operator) {
        searchCriteria = new SearchCriteria(key, operator, value);
    }
}

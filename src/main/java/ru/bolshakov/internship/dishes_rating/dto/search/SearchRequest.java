package ru.bolshakov.internship.dishes_rating.dto.search;

import ru.bolshakov.internship.dishes_rating.service.specification.SearchOperationType;

import java.util.Objects;

public class SearchRequest {

    private String parameter;

    private String value;

    private SearchOperationType operator;

    public SearchRequest() {
    }

    public SearchRequest(String parameter, String value, SearchOperationType operator) {
        this.parameter = parameter;
        this.value = value;
        this.operator = operator;
    }

    public boolean isEmpty() {
        return parameter == null
                || value == null
                || operator == null;
    }

    public String getParameter() {
        return parameter;
    }

    public void setParameter(String parameter) {
        this.parameter = parameter;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public SearchOperationType getOperator() {
        return operator;
    }

    public void setOperator(SearchOperationType operator) {
        this.operator = operator;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SearchRequest that = (SearchRequest) o;
        return Objects.equals(parameter, that.parameter) &&
                Objects.equals(value, that.value) &&
                Objects.equals(operator, that.operator);
    }

    @Override
    public int hashCode() {
        return Objects.hash(parameter, value, operator);
    }
}

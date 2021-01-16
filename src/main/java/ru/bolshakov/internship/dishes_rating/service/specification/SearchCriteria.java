package ru.bolshakov.internship.dishes_rating.service.specification;

public class SearchCriteria {
    private String key;
    private SearchOperationType operationType;
    private Object value;

    public SearchCriteria(String key, SearchOperationType operationType, Object value) {
        this.key = key;
        this.operationType = operationType;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public SearchOperationType getOperationType() {
        return operationType;
    }

    public void setOperationType(SearchOperationType operationType) {
        this.operationType = operationType;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }
}

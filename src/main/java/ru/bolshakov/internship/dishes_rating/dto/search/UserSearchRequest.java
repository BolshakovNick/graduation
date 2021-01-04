package ru.bolshakov.internship.dishes_rating.dto.search;

import java.util.Objects;

public class UserSearchRequest {

    private String userName;

    private boolean startWith;

    private boolean endWith;

    public UserSearchRequest() {
    }

    public UserSearchRequest(String userName, boolean startWith, boolean endWith) {
        this.userName = userName;
        this.startWith = startWith;
        this.endWith = endWith;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public boolean isStartWith() {
        return startWith;
    }

    public void setStartWith(boolean startWith) {
        this.startWith = startWith;
    }

    public boolean isEndWith() {
        return endWith;
    }

    public void setEndWith(boolean endWith) {
        this.endWith = endWith;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserSearchRequest that = (UserSearchRequest) o;
        return Objects.equals(userName, that.userName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userName);
    }
}
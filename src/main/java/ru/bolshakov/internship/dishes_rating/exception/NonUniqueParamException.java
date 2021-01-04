package ru.bolshakov.internship.dishes_rating.exception;

public class NonUniqueParamException extends RuntimeException {

    public NonUniqueParamException(String message) {
        super(message);
    }
}
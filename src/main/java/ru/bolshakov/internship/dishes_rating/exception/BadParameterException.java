package ru.bolshakov.internship.dishes_rating.exception;

public class BadParameterException extends RuntimeException{
    public BadParameterException(String message) {
        super(message);
    }
}

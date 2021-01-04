package ru.bolshakov.internship.dishes_rating.exception;

public class AuthorizationFailureException extends RuntimeException {
    public AuthorizationFailureException(String message) {
        super(message);
    }

    public AuthorizationFailureException(String message, Throwable cause) {
        super(message, cause);
    }
}

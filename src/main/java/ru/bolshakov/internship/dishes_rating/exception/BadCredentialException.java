package ru.bolshakov.internship.dishes_rating.exception;

public class BadCredentialException extends AuthorizationFailureException {
    public BadCredentialException(String message) {
        super(message);
    }
}

package ru.bolshakov.internship.dishes_rating.exception;

public class NonExistentEmailException extends AuthorizationFailureException {
    public NonExistentEmailException(String message) {
        super(message);
    }
}

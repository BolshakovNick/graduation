package ru.bolshakov.internship.dishes_rating.exception;


import org.springframework.security.access.AccessDeniedException;

public class TokenExpiredException extends AccessDeniedException {
    public TokenExpiredException(String message) {
        super(message);
    }
}

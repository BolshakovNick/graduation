package ru.bolshakov.internship.dishes_rating.exception;

public class MessageSendingException extends RuntimeException{
    public MessageSendingException(String message, Throwable cause) {
        super(message, cause);
    }
}

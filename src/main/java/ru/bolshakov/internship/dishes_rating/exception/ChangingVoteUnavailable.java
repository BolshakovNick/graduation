package ru.bolshakov.internship.dishes_rating.exception;

public class ChangingVoteUnavailable extends RuntimeException {
    public ChangingVoteUnavailable(String message) {
        super(message);
    }
}

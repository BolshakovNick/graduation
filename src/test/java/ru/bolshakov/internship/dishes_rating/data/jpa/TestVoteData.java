package ru.bolshakov.internship.dishes_rating.data.jpa;

import ru.bolshakov.internship.dishes_rating.model.Restaurant;
import ru.bolshakov.internship.dishes_rating.model.User;
import ru.bolshakov.internship.dishes_rating.model.Vote;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static ru.bolshakov.internship.dishes_rating.model.Role.USER;

public class TestVoteData {
    public final LocalDateTime today = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);

    public final User existentUser = new User(1L, "username1", "user1@mail.com", "password", USER);
    private final User user2 = new User(2L, "username2", "user2@mail.com", "password", USER);
    private final User user3 = new User(3L, "username3", "user3@mail.com", "password", USER);
    public final Restaurant existentRestaurant = new Restaurant(1L, "restaurant1", "description1");

    public final Vote TEST_VOTE_1 = new Vote(existentUser, today, existentRestaurant);
    public final Vote TEST_VOTE_2 = new Vote(user2, today, existentRestaurant);
    public final Vote TEST_VOTE_3 = new Vote(user3, today, existentRestaurant);

    public Long existentVoteId;
    public final Long nonExistentVoteId = 0L;
}


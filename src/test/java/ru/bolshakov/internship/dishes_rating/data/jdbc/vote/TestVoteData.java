package ru.bolshakov.internship.dishes_rating.data.jdbc.vote;

import ru.bolshakov.internship.dishes_rating.model.jdbc.Vote;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class TestVoteData {
    public final LocalDateTime today = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);

    public final Vote TEST_VOTE_1 = new Vote(1L, today, 1L);
    public final Vote TEST_VOTE_2 = new Vote(2L, today, 1L);
    public final Vote TEST_VOTE_3 = new Vote(3L, today, 1L);

    public final Long existentUserId = 1L;
    public final Long existentRestaurantId = 1L;


    public Long existentVoteId;
    public final Long nonExistentVoteId = 0L;
}

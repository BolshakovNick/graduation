package ru.bolshakov.internship.dishes_rating.repository.jdbc.vote;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import ru.bolshakov.internship.dishes_rating.data.jdbc.vote.TestVoteData;
import ru.bolshakov.internship.dishes_rating.model.jdbc.Vote;
import ru.bolshakov.internship.dishes_rating.repository.jdbc.JdbcVoteRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Sql("classpath:scripts/data.sql")
class JdbcVoteRepositoryTest {
    private final TestVoteData data = new TestVoteData();

    @Autowired
    JdbcVoteRepository voteRepository;

    @Autowired
    JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        jdbcTemplate.update("DELETE FROM vote");
        jdbcTemplate.update("DELETE FROM restaurant");
        jdbcTemplate.update("DELETE FROM users");
        jdbcTemplate.update("INSERT INTO users VALUES (1, 'name1', 'email1@mail.com', 'password1', 'USER', true) ");
        jdbcTemplate.update("INSERT INTO users VALUES (2, 'name2', 'email2@mail.com', 'password2', 'USER', true) ");
        jdbcTemplate.update("INSERT INTO users VALUES (3, 'name3', 'email3@mail.com', 'password3', 'USER', true) ");
        jdbcTemplate.update("INSERT INTO users VALUES (4, 'name4', 'email4@mail.com', 'password4', 'USER', true) ");
        jdbcTemplate.update("INSERT INTO restaurant VALUES (1, 'restaurant1 name', 'description for restaurant1') ");
        data.existentVoteId = voteRepository.save(data.TEST_VOTE_1).getId();
        voteRepository.save(data.TEST_VOTE_2);
        voteRepository.save(data.TEST_VOTE_3);
    }

    @Test
    void saveNewVote() {
        Vote newVote = new Vote(data.existentUserId, LocalDateTime.of(2020, 11, 12, 11, 0), data.existentRestaurantId);
        Vote createdVote = voteRepository.save(newVote);
        assertNotNull(createdVote.getId());
        assertEquals(newVote.getVotingDateTime(), createdVote.getVotingDateTime());
        assertEquals(newVote.getUserId(), createdVote.getUserId());
        assertEquals(newVote.getRestaurantId(), createdVote.getRestaurantId());
    }

    @Test
    void saveNewVoteIfUserHasAlreadyVoted() {
        Vote newVote = new Vote(data.existentUserId, data.today, data.existentRestaurantId);
        assertThrows(DataIntegrityViolationException.class, () -> voteRepository.save(newVote));
    }

    @Test
    void update() {
        Vote voteToUpdate = new Vote(data.existentVoteId, data.existentUserId, LocalDateTime.of(1999, 12, 15, 0, 0), data.existentRestaurantId);
        Vote updatedVote = voteRepository.save(voteToUpdate);

        assertEquals(voteToUpdate.getVotingDateTime(), updatedVote.getVotingDateTime());
        assertEquals(voteToUpdate.getUserId(), updatedVote.getUserId());
        assertEquals(voteToUpdate.getRestaurantId(), updatedVote.getRestaurantId());
    }

    @Test
    void updateIfVoteDoesNotExist() {
        Vote returnedVote = voteRepository.save(new Vote(data.existentUserId, data.nonExistentVoteId, LocalDateTime.of(1999, 12, 15, 0, 0), data.existentRestaurantId));
        assertNull(returnedVote);
    }

    @Test
    void get() {
        Vote receivedVote = voteRepository.get(data.existentVoteId);
        assertEquals(data.TEST_VOTE_1.getVotingDateTime().truncatedTo(ChronoUnit.SECONDS), receivedVote.getVotingDateTime().truncatedTo(ChronoUnit.SECONDS));
        assertEquals(data.TEST_VOTE_1.getUserId(), receivedVote.getUserId());
        assertEquals(data.TEST_VOTE_1.getRestaurantId(), receivedVote.getRestaurantId());
    }

    @Test
    void getIfMenuDoesNotExist() {
        assertNull(voteRepository.get(data.nonExistentVoteId));
    }

    @Test
    void delete() {
        assertTrue(voteRepository.delete(data.existentVoteId));
        assertNull(voteRepository.get(data.existentVoteId));
        assertFalse(voteRepository.delete(data.existentVoteId));
    }

    @Test
    void deleteIfMenuDoesNotExist() {
        assertFalse(voteRepository.delete(data.nonExistentVoteId));
    }

    @Test
    void getAll() {
        List<Vote> votes = voteRepository.getAll();
        assertEquals(3, votes.size());
        assertEquals(data.TEST_VOTE_1.getVotingDateTime().truncatedTo(ChronoUnit.SECONDS), votes.get(0).getVotingDateTime().truncatedTo(ChronoUnit.SECONDS));
        assertEquals(data.TEST_VOTE_1.getUserId(), votes.get(0).getUserId());
        assertEquals(data.TEST_VOTE_1.getRestaurantId(), votes.get(0).getRestaurantId());

        assertEquals(data.TEST_VOTE_2.getVotingDateTime().truncatedTo(ChronoUnit.SECONDS), votes.get(1).getVotingDateTime().truncatedTo(ChronoUnit.SECONDS));
        assertEquals(data.TEST_VOTE_2.getUserId(), votes.get(1).getUserId());
        assertEquals(data.TEST_VOTE_2.getRestaurantId(), votes.get(1).getRestaurantId());

        assertEquals(data.TEST_VOTE_3.getVotingDateTime().truncatedTo(ChronoUnit.SECONDS), votes.get(2).getVotingDateTime().truncatedTo(ChronoUnit.SECONDS));
        assertEquals(data.TEST_VOTE_3.getUserId(), votes.get(2).getUserId());
        assertEquals(data.TEST_VOTE_3.getRestaurantId(), votes.get(2).getRestaurantId());
    }

    @Test
    void getByDateIfDateWithoutVotes() {
        List<Vote> votesByDate = voteRepository.getByDate(LocalDate.of(1800, 12, 30));
        assertEquals(0, votesByDate.size());
    }

    @Test
    void getByCurrentDate() {
        List<Vote> votesByDate = voteRepository.getByDate(LocalDate.now());
        assertEquals(3, votesByDate.size());
    }

    @Test
    void getByCurrentDateForCertainUser() {
        assertNotNull(voteRepository.getByDateForCertainUser(LocalDate.now(), data.existentUserId));
    }

    @Test
    void getByCurrentDateForNonExistentUser() {
        assertNull(voteRepository.getByDateForCertainUser(LocalDate.now(), 0L));
    }

    @Test
    void getRestaurantRatingByDate() {
        assertEquals(3L, voteRepository.getRestaurantRatingByDate(LocalDate.now(), data.existentRestaurantId));
    }
}
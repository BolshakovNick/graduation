package ru.bolshakov.internship.dishes_rating.repository.jpa;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.jdbc.Sql;
import ru.bolshakov.internship.dishes_rating.data.jpa.TestVoteData;
import ru.bolshakov.internship.dishes_rating.model.Vote;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Sql({"classpath:scripts/data.sql", "classpath:scripts/populate_before_vote_repo_tests.sql"})
class JpaVoteRepositoryTest {
    private final TestVoteData data = new TestVoteData();

    @Autowired
    JpaVoteRepository voteRepository;

    @BeforeEach
    void setUp() {
        data.existentVoteId = voteRepository.save(data.TEST_VOTE_1).getId();
        voteRepository.save(data.TEST_VOTE_2);
        voteRepository.save(data.TEST_VOTE_3);
    }

    @Test
    void saveNewVote() {
        Vote newVote = new Vote(data.existentUser, LocalDateTime.of(2020, 11, 12, 11, 0), data.existentRestaurant);
        Vote createdVote = voteRepository.save(newVote);
        assertNotNull(createdVote.getId());
        assertEquals(newVote.getVotingDateTime(), createdVote.getVotingDateTime());
        assertEquals(newVote.getUser().getId(), createdVote.getUser().getId());
        assertEquals(newVote.getRestaurant().getId(), createdVote.getRestaurant().getId());
    }

    @Test
    void saveNewVoteIfUserHasAlreadyVoted() {
        Vote newVote = new Vote(data.existentUser, data.today, data.existentRestaurant);
        assertThrows(DataIntegrityViolationException.class, () -> voteRepository.save(newVote));
    }

    @Test
    void update() {
        Vote voteToUpdate = new Vote(data.existentVoteId, data.existentUser, LocalDateTime.of(1999, 12, 15, 0, 0), data.existentRestaurant);
        Vote updatedVote = voteRepository.save(voteToUpdate);

        assertEquals(voteToUpdate.getVotingDateTime(), updatedVote.getVotingDateTime());
        assertEquals(voteToUpdate.getUser().getId(), updatedVote.getUser().getId());
        assertEquals(voteToUpdate.getRestaurant().getId(), updatedVote.getRestaurant().getId());
    }

    @Test
    void get() {
        Vote receivedVote = voteRepository.findById(data.existentVoteId).orElseThrow();
        assertEquals(data.TEST_VOTE_1.getVotingDateTime().truncatedTo(ChronoUnit.SECONDS), receivedVote.getVotingDateTime().truncatedTo(ChronoUnit.SECONDS));
        assertEquals(data.TEST_VOTE_1.getUser().getId(), receivedVote.getUser().getId());
        assertEquals(data.TEST_VOTE_1.getRestaurant().getId(), receivedVote.getRestaurant().getId());
    }

    @Test
    void getIfMenuDoesNotExist() {
        assertEquals(Optional.empty(), voteRepository.findById(data.nonExistentVoteId));
    }

    @Test
    void delete() {
        assertDoesNotThrow(() -> voteRepository.deleteById(data.existentVoteId));
        assertEquals(Optional.empty(), voteRepository.findById(data.existentVoteId));
        assertThrows(EmptyResultDataAccessException.class, () -> voteRepository.deleteById(data.existentVoteId));
    }

    @Test
    void deleteIfMenuDoesNotExist() {
        assertThrows(EmptyResultDataAccessException.class, () -> voteRepository.deleteById(data.nonExistentVoteId));
    }

    @Test
    void getAll() {
        List<Vote> votes = voteRepository.findAll();
        assertEquals(3, votes.size());
        assertEquals(data.TEST_VOTE_1.getVotingDateTime().truncatedTo(ChronoUnit.SECONDS), votes.get(0).getVotingDateTime().truncatedTo(ChronoUnit.SECONDS));
        assertEquals(data.TEST_VOTE_1.getUser().getId(), votes.get(0).getUser().getId());
        assertEquals(data.TEST_VOTE_1.getRestaurant().getId(), votes.get(0).getRestaurant().getId());

        assertEquals(data.TEST_VOTE_2.getVotingDateTime().truncatedTo(ChronoUnit.SECONDS), votes.get(1).getVotingDateTime().truncatedTo(ChronoUnit.SECONDS));
        assertEquals(data.TEST_VOTE_2.getUser().getId(), votes.get(1).getUser().getId());
        assertEquals(data.TEST_VOTE_2.getRestaurant().getId(), votes.get(1).getRestaurant().getId());

        assertEquals(data.TEST_VOTE_3.getVotingDateTime().truncatedTo(ChronoUnit.SECONDS), votes.get(2).getVotingDateTime().truncatedTo(ChronoUnit.SECONDS));
        assertEquals(data.TEST_VOTE_3.getUser().getId(), votes.get(2).getUser().getId());
        assertEquals(data.TEST_VOTE_3.getRestaurant().getId(), votes.get(2).getRestaurant().getId());
    }

    @Test
    void findAllByVotingDateTimeBetween() {
        List<Vote> votesByDate = voteRepository.findAllByVotingDateTimeBetween(
                LocalDateTime.of(LocalDate.of(2020, 10, 10), LocalTime.MIN),
                LocalDateTime.of(LocalDate.of(2020, 10, 10), LocalTime.MAX));
        assertEquals(0, votesByDate.size());
    }

    @Test
    void findAllByCurrentVotingDateTime() {
        List<Vote> votesByDate = voteRepository.findAllByVotingDateTimeBetween(
                LocalDateTime.of(LocalDate.now(), LocalTime.MIN),
                LocalDateTime.of(LocalDate.now(), LocalTime.MAX));
        assertEquals(3, votesByDate.size());
    }

    @Test
    void findByVotingDateTimeBetweenAndUserId() {
        assertNotEquals(Optional.empty(), voteRepository.findByVotingDateTimeBetweenAndUser_Id(
                LocalDateTime.of(LocalDate.now(), LocalTime.MIN),
                LocalDateTime.of(LocalDate.now(), LocalTime.MAX),
                data.existentUser.getId()));
    }

    @Test
    void findByVotingDateTimeBetweenAndUserIdIsNonExistent() {
        assertEquals(Optional.empty(), voteRepository.findByVotingDateTimeBetweenAndUser_Id(
                LocalDateTime.of(LocalDate.now(), LocalTime.MIN),
                LocalDateTime.of(LocalDate.now(), LocalTime.MAX),
                0L));
    }

    @Test
    void getRestaurantRatingByDate() {
        assertNotNull(voteRepository.countByVotingDateTimeBetweenAndRestaurant_Id(
                LocalDateTime.of(LocalDate.now(), LocalTime.MIN),
                LocalDateTime.of(LocalDate.now(), LocalTime.MAX),
                data.existentRestaurant.getId()).orElse(null));
    }
}

package ru.bolshakov.internship.dishes_rating.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.EmptyResultDataAccessException;
import ru.bolshakov.internship.dishes_rating.dto.vote.VoteDTO;
import ru.bolshakov.internship.dishes_rating.exception.ChangingVoteUnavailable;
import ru.bolshakov.internship.dishes_rating.exception.NotFoundException;
import ru.bolshakov.internship.dishes_rating.model.Role;
import ru.bolshakov.internship.dishes_rating.model.Restaurant;
import ru.bolshakov.internship.dishes_rating.model.User;
import ru.bolshakov.internship.dishes_rating.model.Vote;
import ru.bolshakov.internship.dishes_rating.properties.VotingProperties;
import ru.bolshakov.internship.dishes_rating.repository.jpa.JpaRestaurantRepository;
import ru.bolshakov.internship.dishes_rating.repository.jpa.JpaUserRepository;
import ru.bolshakov.internship.dishes_rating.repository.jpa.JpaVoteRepository;
import ru.bolshakov.internship.dishes_rating.service.mapper.VoteMapper;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class VoteServiceTest {
    private static final LocalDate TODAY = LocalDate.now();
    private static final Restaurant EXISTENT_RESTAURANT = new Restaurant(123L, "restaurant1", "description1");
    private static final User EXISTENT_USER = new User(53L, "userName1", "user1@mail.com", "password", Role.USER);
    private static final Vote NEW_VOTE = new Vote(EXISTENT_USER, LocalDateTime.now(), EXISTENT_RESTAURANT);
    private static final Vote EXISTENT_VOTE = new Vote(1L, NEW_VOTE.getUser(), NEW_VOTE.getVotingDateTime(), NEW_VOTE.getRestaurant());
    private static final Long NON_EXISTENT_VOTE_ID = 1L;

    @Mock
    private JpaVoteRepository repository;

    @Mock
    private VotingProperties properties;

    @Mock
    private JpaRestaurantRepository restaurantRepository;

    @Mock
    private JpaUserRepository userRepository;

    @Spy
    private VoteMapper mapper;

    @InjectMocks
    VoteService service;

    @Test
    void create() {
        Mockito.when(restaurantRepository.findById(EXISTENT_RESTAURANT.getId())).thenReturn(Optional.of(EXISTENT_RESTAURANT));
        Mockito.when(userRepository.findById(EXISTENT_USER.getId())).thenReturn(Optional.of(EXISTENT_USER));
        Mockito.when(repository.save(NEW_VOTE))
                .thenReturn(EXISTENT_VOTE);
        Mockito.when(repository.findByVotingDateTimeBetweenAndUser_Id(
                LocalDateTime.of(TODAY, LocalTime.MIN),
                LocalDateTime.of(TODAY, LocalTime.MAX),
                NEW_VOTE.getUser().getId()))
                .thenReturn(Optional.empty());
        Mockito.when(properties.getBoundaryTime()).thenReturn(LocalTime.now().plus(1, ChronoUnit.MINUTES));

        VoteDTO createdVoteDTO = service.vote(NEW_VOTE.getUser().getId(), NEW_VOTE.getRestaurant().getId());
        assertNotNull(createdVoteDTO.getId());
        assertEquals(NEW_VOTE.getUser().getId(), createdVoteDTO.getUser().getId());
        assertEquals(NEW_VOTE.getVotingDateTime().toLocalTime(), createdVoteDTO.getLocalTime());
        assertEquals(NEW_VOTE.getRestaurant().getId(), createdVoteDTO.getRestaurant().getId());
    }

    @Test
    void createVoteIfRestaurantDoesNotExist() {
        Long restaurantId = 1L;
        Mockito.when(restaurantRepository.findById(restaurantId)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> service.vote(EXISTENT_USER.getId(), restaurantId));
    }

    @Test
    void update() {
        Mockito.when(restaurantRepository.findById(EXISTENT_RESTAURANT.getId())).thenReturn(Optional.of(EXISTENT_RESTAURANT));
        Mockito.when(userRepository.findById(EXISTENT_USER.getId())).thenReturn(Optional.of(EXISTENT_USER));
        Mockito.when(repository.save(EXISTENT_VOTE)).thenReturn(EXISTENT_VOTE);
        Mockito.when(repository.findByVotingDateTimeBetweenAndUser_Id(
                LocalDateTime.of(TODAY, LocalTime.MIN),
                LocalDateTime.of(TODAY, LocalTime.MAX),
                EXISTENT_VOTE.getUser().getId()))
                .thenReturn(Optional.of(EXISTENT_VOTE));
        Mockito.when(properties.getBoundaryTime()).thenReturn(LocalTime.now().plus(1, ChronoUnit.MINUTES));

        VoteDTO updatedVote = service.vote(EXISTENT_VOTE.getUser().getId(), EXISTENT_VOTE.getRestaurant().getId());

        assertEquals(EXISTENT_VOTE.getId(), updatedVote.getId());
        assertEquals(EXISTENT_VOTE.getUser().getId(), updatedVote.getUser().getId());
        assertEquals(EXISTENT_VOTE.getVotingDateTime().toLocalTime(), updatedVote.getLocalTime());
        assertEquals(EXISTENT_VOTE.getRestaurant().getId(), updatedVote.getRestaurant().getId());
    }

    @Test
    void updateIfTimeMoreThanAllowed() {
        Mockito.when(restaurantRepository.findById(EXISTENT_RESTAURANT.getId())).thenReturn(Optional.of(EXISTENT_RESTAURANT));
        Mockito.when(userRepository.findById(EXISTENT_USER.getId())).thenReturn(Optional.of(EXISTENT_USER));
        Mockito.when(repository.findByVotingDateTimeBetweenAndUser_Id(
                LocalDateTime.of(TODAY, LocalTime.MIN),
                LocalDateTime.of(TODAY, LocalTime.MAX), EXISTENT_USER.getId()))
                .thenReturn(Optional.of(new Vote(1L, EXISTENT_USER, LocalDateTime.of(LocalDate.now(), LocalTime.now().minus(2, ChronoUnit.HOURS)), EXISTENT_RESTAURANT)));
        Mockito.when(properties.getBoundaryTime()).thenReturn(LocalTime.now().minus(1, ChronoUnit.MINUTES));

        assertThrows(ChangingVoteUnavailable.class, () -> service.vote(EXISTENT_USER.getId(), EXISTENT_RESTAURANT.getId()));
    }

    @Test
    void delete() {
        Mockito.doNothing().when(repository).deleteById(EXISTENT_VOTE.getId());
        Mockito.when(repository.findById(EXISTENT_VOTE.getId())).thenReturn(Optional.empty());
        Mockito.when(properties.getBoundaryTime()).thenReturn(LocalTime.now().plus(1, ChronoUnit.MINUTES));

        assertDoesNotThrow(() -> service.delete(EXISTENT_VOTE.getId()));
        assertThrows(NotFoundException.class, () -> service.get(EXISTENT_VOTE.getId()));
    }

    @Test
    void deleteIfRestaurantDoesNotExist() {
        Mockito.doThrow(EmptyResultDataAccessException.class).when(repository).deleteById(NON_EXISTENT_VOTE_ID);
        Mockito.when(properties.getBoundaryTime()).thenReturn(LocalTime.now().plus(1, ChronoUnit.MINUTES));
        assertThrows(NotFoundException.class, () -> service.delete(NON_EXISTENT_VOTE_ID));
    }

    @Test
    void get() {
        Mockito.when(repository.findById(NEW_VOTE.getId())).thenReturn(Optional.of(NEW_VOTE));

        VoteDTO returnedVote = service.get(NEW_VOTE.getId());

        assertEquals(NEW_VOTE.getId(), returnedVote.getId());
        assertEquals(NEW_VOTE.getUser().getId(), returnedVote.getUser().getId());
        assertEquals(NEW_VOTE.getVotingDateTime().toLocalTime(), returnedVote.getLocalTime());
        assertEquals(NEW_VOTE.getRestaurant().getId(), returnedVote.getRestaurant().getId());
    }

    @Test
    void getIfVoteDoesNotExist() {
        Mockito.when(repository.findById(NON_EXISTENT_VOTE_ID)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> service.get(NON_EXISTENT_VOTE_ID));
    }
}

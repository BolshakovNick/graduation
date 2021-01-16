package ru.bolshakov.internship.dishes_rating.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.bolshakov.internship.dishes_rating.dto.vote.VoteDTO;
import ru.bolshakov.internship.dishes_rating.exception.ChangingVoteUnavailable;
import ru.bolshakov.internship.dishes_rating.exception.NotFoundException;
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
import java.util.Optional;

@Service
public class VoteService {
    protected final Logger log = LoggerFactory.getLogger(VoteService.class);

    private final VotingProperties config;

    private final JpaVoteRepository voteRepository;

    private final JpaRestaurantRepository restaurantRepository;

    private final JpaUserRepository userRepository;

    private final VoteMapper mapper;

    public VoteService(JpaVoteRepository voteRepository,
                       VoteMapper mapper,
                       VotingProperties config,
                       JpaRestaurantRepository restaurantRepository,
                       JpaUserRepository userRepository) {
        this.voteRepository = voteRepository;
        this.mapper = mapper;
        this.config = config;
        this.restaurantRepository = restaurantRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public VoteDTO vote(Long userId, Long restaurantId) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new NotFoundException("Restaurant with such ID not found"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User with such ID not found"));
        LocalTime currentTime = LocalTime.now();

        Vote vote = getVoteIfAlreadyExists(userId)
                .orElse(new Vote(user, LocalDateTime.of(LocalDate.now(),currentTime), restaurant));
        vote.setRestaurant(restaurant);
        if (!isVoteChangingAvailable(currentTime)) {
            log.warn("Updating vote for user with id {} is unavailable, because voting time: {} is more than {}.", vote.getUser(), currentTime, config.getBoundaryTime());
            throw new ChangingVoteUnavailable("Voting has already closed. Current time is more than " + config.getBoundaryTime());
        }
        return mapper.toDTO(voteRepository.save(vote));
    }

    @Transactional
    public void delete(Long id) {
        try {
            LocalTime currentTime = LocalTime.now();
            if (!isVoteChangingAvailable(LocalTime.now())) {
                log.warn("Deleting vote is unavailable, because voting time: {} is more than {}.", currentTime, config.getBoundaryTime());
                throw new ChangingVoteUnavailable("Voting has already closed. Current time is more than " + config.getBoundaryTime());
            }
            voteRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            log.warn("Deleting {} failed. Possible reason: vote with id {} does not exist in database", id, id);
            throw new NotFoundException("Vote with such ID is not found");
        }
    }

    public VoteDTO get(Long id) {
        return mapper.toDTO(voteRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Restaurant with such ID not found")));
    }

    private Optional<Vote> getVoteIfAlreadyExists(Long userId) {
        return voteRepository.findByVotingDateTimeBetweenAndUser_Id(LocalDate.now().atStartOfDay(), LocalDate.now().atTime(LocalTime.MAX), userId);
    }

    private boolean isVoteChangingAvailable(LocalTime time) {
        return config.getBoundaryTime().compareTo(time) > 0;
    }
}

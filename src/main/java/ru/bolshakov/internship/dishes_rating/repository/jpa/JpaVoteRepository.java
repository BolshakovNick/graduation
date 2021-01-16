package ru.bolshakov.internship.dishes_rating.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.bolshakov.internship.dishes_rating.model.Vote;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface JpaVoteRepository extends JpaRepository<Vote, Long> {

    Optional<Long> countByVotingDateTimeBetweenAndRestaurant_Id(LocalDateTime startTime, LocalDateTime endTime, Long restaurantId);

    Optional<Vote> findByVotingDateTimeBetweenAndUser_Id(LocalDateTime startTime, LocalDateTime endTime, Long userId);

    List<Vote> findAllByVotingDateTimeBetween(LocalDateTime startTime, LocalDateTime endTime);
}

package ru.bolshakov.internship.dishes_rating.repository.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;
import ru.bolshakov.internship.dishes_rating.model.jdbc.Vote;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Repository
public class JdbcVoteRepository extends JdbcEntityRepository<Vote> {
    private static final String TABLE_NAME = "vote";

    @Autowired
    public JdbcVoteRepository(RowMapper<Vote> mapper, JdbcTemplate jdbcTemplate) {
        super(mapper, jdbcTemplate, TABLE_NAME);
    }

    public List<Vote> getByDate(LocalDate date) {
        return jdbcTemplate.query(
                "SELECT * FROM " + TABLE_NAME + " WHERE voting_datetime>=? AND voting_datetime<=?",
                mapper, LocalDateTime.of(date, LocalTime.MIN), LocalDateTime.of(date, LocalTime.MAX));
    }

    public Long getRestaurantRatingByDate(LocalDate date, Long restaurantId) {
        return jdbcTemplate.queryForObject("SELECT COUNT(id) from vote " +
                        "WHERE voting_datetime>=? " +
                        "AND voting_datetime<=? " +
                        "AND restaurant_id=?",
                Long.class,
                LocalDateTime.of(date, LocalTime.MIN),
                LocalDateTime.of(date, LocalTime.MAX),
                restaurantId);
    }

    public Vote getByDateForCertainUser(LocalDate date, Long userId) {
        try {
            return jdbcTemplate.queryForObject(
                    "SELECT * FROM " + TABLE_NAME + " WHERE user_id=? AND voting_datetime>=? AND voting_datetime<=?",
                    mapper, userId, LocalDateTime.of(date, LocalTime.MIN), LocalDateTime.of(date, LocalTime.MAX));
        } catch (EmptyResultDataAccessException exception) {
            return null;
        }
    }

    @Override
    protected Vote createNewEntity(Vote vote) {
        MapSqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("id", vote.getId())
                .addValue("voting_datetime", vote.getVotingDateTime())
                .addValue("user_id", vote.getUserId())
                .addValue("restaurant_id", vote.getRestaurantId());
        Number newKey = insertEntity.executeAndReturnKey(parameters);
        vote.setId(newKey.longValue());
        return vote;
    }

    @Override
    protected Vote updateExistentEntity(Vote vote) {
        if (jdbcTemplate.update("" +
                "UPDATE " + TABLE_NAME +
                " SET voting_datetime=?, user_id=?, restaurant_id=? " +
                "WHERE id=?", vote.getVotingDateTime(), vote.getUserId(), vote.getRestaurantId(), vote.getId()) == 0) {
            return null;
        }
        return vote;
    }
}
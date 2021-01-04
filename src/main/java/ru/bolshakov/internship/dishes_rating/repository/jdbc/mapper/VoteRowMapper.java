package ru.bolshakov.internship.dishes_rating.repository.jdbc.mapper;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.bolshakov.internship.dishes_rating.model.jdbc.Vote;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class VoteRowMapper implements RowMapper<Vote> {
    @Override
    public Vote mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Vote(rs.getLong("id"),
                rs.getLong("user_id"),
                rs.getTimestamp("voting_datetime").toLocalDateTime(),
                rs.getLong("restaurant_id"));
    }
}

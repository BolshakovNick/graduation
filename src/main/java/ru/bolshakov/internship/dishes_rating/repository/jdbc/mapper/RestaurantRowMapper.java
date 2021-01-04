package ru.bolshakov.internship.dishes_rating.repository.jdbc.mapper;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.bolshakov.internship.dishes_rating.model.jdbc.Restaurant;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class RestaurantRowMapper implements RowMapper<Restaurant> {

    @Override
    public Restaurant mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Restaurant(rs.getLong("id"),
                rs.getString("restaurant_name"),
                rs.getString("description"));
    }
}
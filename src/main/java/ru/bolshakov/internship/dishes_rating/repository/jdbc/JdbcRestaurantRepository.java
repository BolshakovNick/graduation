package ru.bolshakov.internship.dishes_rating.repository.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;
import ru.bolshakov.internship.dishes_rating.model.jdbc.Restaurant;
import ru.bolshakov.internship.dishes_rating.repository.jdbc.mapper.RestaurantRowMapper;

@Repository
public class JdbcRestaurantRepository extends JdbcEntityRepository<Restaurant> {

    private static final String TABLE_NAME = "restaurant";

    @Autowired
    public JdbcRestaurantRepository(JdbcTemplate jdbcTemplate, RestaurantRowMapper mapper) {
        super(mapper, jdbcTemplate, TABLE_NAME);
    }

    public Restaurant getByName(String name) {
        try {
            return jdbcTemplate.queryForObject("SELECT * FROM " + TABLE_NAME + " WHERE restaurant_name=?", mapper, name);
        } catch (EmptyResultDataAccessException exception) {
            return null;
        }
    }

    @Override
    protected Restaurant createNewEntity(Restaurant restaurant) {
        MapSqlParameterSource parameterSource = new MapSqlParameterSource()
                .addValue("id", restaurant.getId())
                .addValue("restaurant_name", restaurant.getRestaurantName())
                .addValue("description", restaurant.getDescription());
        Number newKey = insertEntity.executeAndReturnKey(parameterSource);
        restaurant.setId(newKey.longValue());
        return restaurant;
    }

    @Override
    protected Restaurant updateExistentEntity(Restaurant restaurant) {
        if (jdbcTemplate.update("" +
                "UPDATE " + TABLE_NAME +
                " SET restaurant_name=?, description=? " +
                "WHERE id=?", restaurant.getRestaurantName(), restaurant.getDescription(), restaurant.getId()) == 0) {
            return null;
        }
        return restaurant;
    }
}

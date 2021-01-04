package ru.bolshakov.internship.dishes_rating.repository.jdbc.mapper;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.bolshakov.internship.dishes_rating.model.jdbc.Dish;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class DishRowMapper implements RowMapper<Dish> {
    @Override
    public Dish mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Dish(rs.getLong("id"),
                rs.getString("dish_name"),
                rs.getLong("price"),
                rs.getLong("menu_id"));
    }
}

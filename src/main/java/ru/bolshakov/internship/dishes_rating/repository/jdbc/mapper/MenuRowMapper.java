package ru.bolshakov.internship.dishes_rating.repository.jdbc.mapper;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.bolshakov.internship.dishes_rating.model.jdbc.Menu;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class MenuRowMapper implements RowMapper<Menu> {
    @Override
    public Menu mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Menu(rs.getLong("id"),
                rs.getDate("menu_date").toLocalDate(),
                rs.getLong("restaurant_id"));
    }
}
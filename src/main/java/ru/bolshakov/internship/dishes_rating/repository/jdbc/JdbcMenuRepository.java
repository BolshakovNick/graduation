package ru.bolshakov.internship.dishes_rating.repository.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;
import ru.bolshakov.internship.dishes_rating.model.jdbc.Menu;

import java.time.LocalDate;
import java.util.List;

@Repository
public class JdbcMenuRepository extends JdbcEntityRepository<Menu> {
    private static final String TABLE_NAME = "menu";

    @Autowired
    public JdbcMenuRepository(RowMapper<Menu> mapper, JdbcTemplate jdbcTemplate) {
        super(mapper, jdbcTemplate, TABLE_NAME);
    }

    public List<Menu> getByRestaurant(Long restaurantId) {
        return jdbcTemplate.query(
                "SELECT * FROM " + TABLE_NAME + " WHERE restaurant_id=?",
                mapper, restaurantId);
    }

    public Menu getByDateForCertainRestaurant(LocalDate date, Long restaurantId) {
        try {
            return jdbcTemplate.queryForObject(
                    "SELECT * FROM " + TABLE_NAME + " WHERE restaurant_id=? AND menu_date=?",
                    mapper, restaurantId, date);
        } catch (EmptyResultDataAccessException exception) {
            return null;
        }
    }

    @Override
    protected Menu createNewEntity(Menu menu) {
        MapSqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("id", menu.getId())
                .addValue("menu_date", menu.getMenuDate())
                .addValue("restaurant_id", menu.getRestaurantId());
        Number newKey = insertEntity.executeAndReturnKey(parameters);
        menu.setId(newKey.longValue());
        return menu;
    }

    @Override
    protected Menu updateExistentEntity(Menu menu) {
        if (jdbcTemplate.update("" +
                "UPDATE " + TABLE_NAME +
                " SET menu_date=?, restaurant_id=? " +
                "WHERE id=?", menu.getMenuDate(), menu.getRestaurantId(), menu.getId()) == 0) {
            return null;
        }
        return menu;
    }
}

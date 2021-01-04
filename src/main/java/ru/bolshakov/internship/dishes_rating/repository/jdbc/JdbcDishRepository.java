package ru.bolshakov.internship.dishes_rating.repository.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;
import ru.bolshakov.internship.dishes_rating.model.jdbc.Dish;

import java.util.List;

@Repository
public class JdbcDishRepository extends JdbcEntityRepository<Dish> {
    private static final String TABLE_NAME = "dish";

    @Autowired
    public JdbcDishRepository(RowMapper<Dish> mapper, JdbcTemplate jdbcTemplate) {
        super(mapper, jdbcTemplate, TABLE_NAME);
    }

    @Override
    protected Dish createNewEntity(Dish dish) {
        MapSqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("id", dish.getId())
                .addValue("menu_id", dish.getMenuId())
                .addValue("dish_name", dish.getName())
                .addValue("price", dish.getPrice());
        Number newKey = insertEntity.executeAndReturnKey(parameters);
        dish.setId(newKey.longValue());
        return dish;
    }

    @Override
    protected Dish updateExistentEntity(Dish dish) {
        if (jdbcTemplate.update("" +
                "UPDATE " + TABLE_NAME +
                " SET menu_id=?, dish_name=?, price=? " +
                "WHERE id=?", dish.getMenuId(), dish.getName(), dish.getPrice(), dish.getId()) == 0) {
            return null;
        }
        return dish;
    }

    public List<Dish> getAll(Long menuId) {
        return jdbcTemplate.query("SELECT * FROM " + TABLE_NAME + " WHERE menu_id=?", mapper, menuId);
    }
}

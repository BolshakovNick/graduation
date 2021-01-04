package ru.bolshakov.internship.dishes_rating.repository.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;
import ru.bolshakov.internship.dishes_rating.model.jdbc.User;
import ru.bolshakov.internship.dishes_rating.repository.jdbc.mapper.UserRowMapper;

@Repository
public class JdbcUserRepository extends JdbcEntityRepository<User> {

    private static final String TABLE_NAME = "users";

    @Autowired
    public JdbcUserRepository(JdbcTemplate jdbcTemplate, UserRowMapper mapper) {
        super(mapper, jdbcTemplate, TABLE_NAME);
    }

    public User getByEmail(String email) {
        try {
            return jdbcTemplate.queryForObject("SELECT * FROM " + TABLE_NAME + " WHERE email=?", mapper, email);
        } catch (EmptyResultDataAccessException exception) {
            return null;
        }
    }

    @Override
    protected User createNewEntity(User user) {
        MapSqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("id", user.getId())
                .addValue("user_name", user.getUserName())
                .addValue("email", user.getEmail())
                .addValue("password", user.getPassword())
                .addValue("role", user.getRole().toString());
        Number newKey = insertEntity.executeAndReturnKey(parameters);
        user.setId(newKey.longValue());
        return user;
    }

    @Override
    protected User updateExistentEntity(User user) {
        if (jdbcTemplate.update("" +
                "UPDATE " + TABLE_NAME +
                " SET user_name=?, email=?, password=?, role=? " +
                "WHERE id=?", user.getUserName(), user.getEmail(), user.getPassword(), user.getRole().name(), user.getId()) == 0) {
            return null;
        }
        return user;
    }
}
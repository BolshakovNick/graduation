package ru.bolshakov.internship.dishes_rating.repository.jdbc;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import ru.bolshakov.internship.dishes_rating.model.jdbc.BaseEntity;

import java.util.List;

public abstract class JdbcEntityRepository<T extends BaseEntity> implements EntityRepository<T> {
    private final String tableName;

    protected final RowMapper<T> mapper;

    protected final JdbcTemplate jdbcTemplate;

    protected final SimpleJdbcInsert insertEntity;

    public JdbcEntityRepository(RowMapper<T> mapper, JdbcTemplate jdbcTemplate, String tableName) {
        this.tableName = tableName;
        this.mapper = mapper;
        this.jdbcTemplate = jdbcTemplate;
        this.insertEntity = new SimpleJdbcInsert(jdbcTemplate).withTableName(tableName).usingGeneratedKeyColumns("id");
    }

    @Override
    public T save(T entity) {
        if (entity.isNew()) {
            return createNewEntity(entity);
        } else {
            return updateExistentEntity(entity);
        }
    }

    @Override
    public boolean delete(Long id) {
        return jdbcTemplate.update("DELETE FROM " + tableName + " WHERE id=?", id) != 0;
    }

    @Override
    public T get(Long id) {
        try {
            return jdbcTemplate.queryForObject("SELECT * FROM " + tableName + " WHERE id=?", mapper, id);
        } catch (EmptyResultDataAccessException exception) {
            return null;
        }
    }

    @Override
    public List<T> getAll() {
        return jdbcTemplate.query("SELECT * FROM " + tableName, mapper);
    }

    protected abstract T createNewEntity(T entity);

    protected abstract T updateExistentEntity(T entity);
}

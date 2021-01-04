package ru.bolshakov.internship.dishes_rating.repository.jdbc.dish;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import ru.bolshakov.internship.dishes_rating.data.jdbc.dish.TestDishData;
import ru.bolshakov.internship.dishes_rating.model.jdbc.Dish;
import ru.bolshakov.internship.dishes_rating.repository.jdbc.JdbcDishRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Sql("classpath:scripts/data.sql")
class JdbcDishRepositoryTest {
    private final TestDishData data = new TestDishData();

    @Autowired
    JdbcDishRepository dishRepository;

    @Autowired
    JdbcTemplate jdbcTemplate;

    private final Long existentMenuId = 1L;

    @BeforeEach
    void setUp() {
        jdbcTemplate.update("DELETE FROM dish");
        jdbcTemplate.update("DELETE FROM menu");
        jdbcTemplate.update("DELETE FROM restaurant");
        jdbcTemplate.update("INSERT INTO restaurant VALUES (1, 'restaurant name', 'description for restaurant') ");
        jdbcTemplate.update("INSERT INTO menu VALUES (1, now(), 1)");
        data.existentDishId = dishRepository.save(data.TEST_DISH_1).getId();
        dishRepository.save(data.TEST_DISH_2);
        dishRepository.save(data.TEST_DISH_3);
    }

    @Test
    void saveNewDish() {
        Dish newDish = new Dish("new Dish", 20000L, existentMenuId);
        Dish createdDish = dishRepository.save(newDish);
        assertNotNull(createdDish.getId());
        assertEquals(newDish.getMenuId(), createdDish.getMenuId());
        assertEquals(newDish.getName(), createdDish.getName());
        assertEquals(newDish.getPrice(), createdDish.getPrice());
    }

    @Test
    void saveNewDishIfNameIsLongerThanConstraint() {
        Dish newDish = new Dish("qwertyuiopasdfghjklzxcvbnmqwertyuiopasdfghjklzxcvbnmm", 39990L, existentMenuId);
        assertThrows(org.springframework.dao.DataIntegrityViolationException.class, () -> dishRepository.save(newDish));
    }

    @Test
    void update() {
        Dish dishToUpdate = new Dish(data.existentDishId, "Updated Dish Name", 20000L, existentMenuId);
        Dish updatedDish = dishRepository.save(dishToUpdate);
        assertEquals(dishToUpdate.getMenuId(), updatedDish.getMenuId());
        assertEquals(dishToUpdate.getName(), updatedDish.getName());
        assertEquals(dishToUpdate.getPrice(), updatedDish.getPrice());
    }

    @Test
    void updateIfDishDoesNotExist() {
        Dish returnedDish = dishRepository.save(new Dish(data.nonExistentDishId, "Updated Dish Name", 20000L, existentMenuId));
        assertNull(returnedDish);
    }

    @Test
    void get() {
        Dish receivedDish = dishRepository.get(data.existentDishId);
        assertEquals(data.TEST_DISH_1.getMenuId(), receivedDish.getMenuId());
        assertEquals(data.TEST_DISH_1.getName(), receivedDish.getName());
        assertEquals(data.TEST_DISH_1.getPrice(), receivedDish.getPrice());
    }

    @Test
    void getIfDishDoesNotExist() {
        assertNull(dishRepository.get(data.nonExistentDishId));
    }

    @Test
    void delete() {
        assertTrue(dishRepository.delete(data.existentDishId));
        assertNull(dishRepository.get(data.existentDishId));
        assertFalse(dishRepository.delete(data.existentDishId));
    }

    @Test
    void deleteIfDishDoesNotExist() {
        assertFalse(dishRepository.delete(data.nonExistentDishId));
    }

    @Test
    void getAll() {
        List<Dish> dishes = dishRepository.getAll();
        assertEquals(3, dishes.size());
        assertEquals(data.TEST_DISH_1.getMenuId(), dishes.get(0).getMenuId());
        assertEquals(data.TEST_DISH_1.getName(), dishes.get(0).getName());
        assertEquals(data.TEST_DISH_1.getPrice(), dishes.get(0).getPrice());

        assertEquals(data.TEST_DISH_2.getMenuId(), dishes.get(1).getMenuId());
        assertEquals(data.TEST_DISH_2.getName(), dishes.get(1).getName());
        assertEquals(data.TEST_DISH_2.getPrice(), dishes.get(1).getPrice());

        assertEquals(data.TEST_DISH_3.getMenuId(), dishes.get(2).getMenuId());
        assertEquals(data.TEST_DISH_3.getName(), dishes.get(2).getName());
        assertEquals(data.TEST_DISH_3.getPrice(), dishes.get(2).getPrice());
    }
}
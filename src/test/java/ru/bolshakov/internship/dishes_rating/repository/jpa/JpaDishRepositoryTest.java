package ru.bolshakov.internship.dishes_rating.repository.jpa;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.jdbc.Sql;
import ru.bolshakov.internship.dishes_rating.data.jpa.TestDishData;
import ru.bolshakov.internship.dishes_rating.model.Dish;
import ru.bolshakov.internship.dishes_rating.model.Menu;
import ru.bolshakov.internship.dishes_rating.model.Restaurant;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Sql({"classpath:scripts/data.sql", "classpath:scripts/populate_before_dish_repo_test.sql"})
class JpaDishRepositoryTest {
    private final TestDishData data = new TestDishData();

    @Autowired
    JpaDishRepository dishRepository;

    private final Menu existentMenu = new Menu(1L, LocalDate.now(), new Restaurant(1L, "restaurant name", "description for restaurant"));

    @BeforeEach
    void setUp() {
        data.existentDishId = dishRepository.save(data.TEST_DISH_1).getId();
        dishRepository.save(data.TEST_DISH_2);
        dishRepository.save(data.TEST_DISH_3);
    }

    @Test
    void saveNewDish() {
        Dish newDish = new Dish("new Dish", 20000L, existentMenu);
        Dish createdDish = dishRepository.save(newDish);
        assertNotNull(createdDish.getId());
        assertEquals(newDish.getMenu(), createdDish.getMenu());
        assertEquals(newDish.getName(), createdDish.getName());
        assertEquals(newDish.getPrice(), createdDish.getPrice());
    }

    @Test
    void saveNewDishIfNameIsLongerThanConstraint() {
        Dish newDish = new Dish("qwertyuiopasdfghjklzxcvbnmqwertyuiopasdfghjklzxcvbnmm", 39990L, existentMenu);
        assertThrows(DataIntegrityViolationException.class, () -> dishRepository.save(newDish));
    }

    @Test
    void update() {
        Dish dishToUpdate = new Dish(data.existentDishId, "Updated Dish Name", 20000L, existentMenu);
        Dish updatedDish = dishRepository.save(dishToUpdate);

        assertEquals(dishToUpdate.getName(), updatedDish.getName());
        assertEquals(dishToUpdate.getPrice(), updatedDish.getPrice());
    }

    @Test
    void get() {
        Dish receivedDish = dishRepository.findById(data.existentDishId).orElseThrow();

        assertEquals(data.TEST_DISH_1.getName(), receivedDish.getName());
        assertEquals(data.TEST_DISH_1.getPrice(), receivedDish.getPrice());
    }

    @Test
    void getIfDishDoesNotExist() {
        assertEquals(Optional.empty(), dishRepository.findById(data.nonExistentDishId));
    }

    @Test
    void delete() {
        assertDoesNotThrow(() -> dishRepository.deleteById(data.existentDishId));
        assertEquals(Optional.empty(), dishRepository.findById(data.existentDishId));
        assertThrows(EmptyResultDataAccessException.class, () -> dishRepository.deleteById(data.existentDishId));
    }

    @Test
    void deleteIfDishDoesNotExist() {
        assertThrows(EmptyResultDataAccessException.class, () -> dishRepository.deleteById(data.nonExistentDishId));
    }

    @Test
    void getAll() {
        List<Dish> dishes = dishRepository.findAll();
        assertEquals(3, dishes.size());

        assertEquals(data.TEST_DISH_1.getName(), dishes.get(0).getName());
        assertEquals(data.TEST_DISH_1.getPrice(), dishes.get(0).getPrice());

        assertEquals(data.TEST_DISH_2.getName(), dishes.get(1).getName());
        assertEquals(data.TEST_DISH_2.getPrice(), dishes.get(1).getPrice());

        assertEquals(data.TEST_DISH_3.getName(), dishes.get(2).getName());
        assertEquals(data.TEST_DISH_3.getPrice(), dishes.get(2).getPrice());
    }
}

package ru.bolshakov.internship.dishes_rating.repository.jdbc.restaurant;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import ru.bolshakov.internship.dishes_rating.data.jdbc.restaurant.TestRestaurantData;
import ru.bolshakov.internship.dishes_rating.model.jdbc.Restaurant;
import ru.bolshakov.internship.dishes_rating.repository.jdbc.JdbcRestaurantRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
@Sql("classpath:scripts/data.sql")
class JdbcRestaurantRepositoryTest {
    TestRestaurantData data = new TestRestaurantData();

    @Autowired
    private JdbcRestaurantRepository restaurantRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        jdbcTemplate.update("DELETE FROM restaurant");
        data.existentRestaurantId = restaurantRepository.save(data.TEST_RESTAURANT_1).getId();
        restaurantRepository.save(data.TEST_RESTAURANT_2);
        restaurantRepository.save(data.TEST_RESTAURANT_3);
    }

    @Test
    void create() {
        Restaurant newRestaurant = new Restaurant("newRestaurant", "new description");
        Restaurant createdRestaurant = restaurantRepository.save(newRestaurant);
        Assertions.assertNotNull(createdRestaurant.getId());
        Assertions.assertEquals(newRestaurant.getRestaurantName(), createdRestaurant.getRestaurantName());
        Assertions.assertEquals(newRestaurant.getDescription(), createdRestaurant.getDescription());
    }

    @Test
    void createIfNameIsNotUnique() {
        Restaurant newRestaurant = new Restaurant(data.TEST_RESTAURANT_1.getRestaurantName(), "new description");
        Assertions.assertThrows(DataIntegrityViolationException.class, () -> restaurantRepository.save(newRestaurant));
    }

    @Test
    void createIfNameIsLongerThanConstraint() {
        Restaurant newRestaurant = new Restaurant("qwertyuiopasdfghjklzxcvbnmqwertyuiopasdfghjklzxcvbnmm", "new description");
        Assertions.assertThrows(DataIntegrityViolationException.class, () -> restaurantRepository.save(newRestaurant));
    }

    @Test
    void delete() {
        Assertions.assertTrue(restaurantRepository.delete(data.existentRestaurantId));
    }

    @Test
    void deleteIfRestaurantDoesNotExist() {
        Assertions.assertFalse(restaurantRepository.delete(data.nonExistentRestaurantId));
    }

    @Test
    void get() {
        assertThat(restaurantRepository.get(data.existentRestaurantId))
                .isEqualToIgnoringGivenFields(data.TEST_RESTAURANT_1, "id");
    }

    @Test
    void getIfRestaurantDoesNotExist() {
       Assertions.assertNull(restaurantRepository.get(data.nonExistentRestaurantId));
    }

    @Test
    void getAll() {
        List<Restaurant> restaurants = restaurantRepository.getAll();
        Assertions.assertEquals(3, restaurants.size());
        Assertions.assertEquals(data.TEST_RESTAURANT_1.getRestaurantName(), restaurants.get(0).getRestaurantName());
        Assertions.assertEquals(data.TEST_RESTAURANT_2.getRestaurantName(), restaurants.get(1).getRestaurantName());
        Assertions.assertEquals(data.TEST_RESTAURANT_3.getRestaurantName(), restaurants.get(2).getRestaurantName());
    }

    @Test
    void getByName() {
        Restaurant returnedRestaurant = restaurantRepository.getByName(data.TEST_RESTAURANT_1.getRestaurantName());
        Assertions.assertEquals(data.existentRestaurantId,returnedRestaurant.getId());
        Assertions.assertEquals(data.TEST_RESTAURANT_1.getRestaurantName(), returnedRestaurant.getRestaurantName());
        Assertions.assertEquals(data.TEST_RESTAURANT_1.getDescription(), returnedRestaurant.getDescription());
    }

    @Test
    void getByNameIfRestaurantDoesNotExist() {
        Assertions.assertNull( restaurantRepository.getByName("notExistentName"));

    }
}
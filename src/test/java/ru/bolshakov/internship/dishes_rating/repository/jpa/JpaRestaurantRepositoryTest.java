package ru.bolshakov.internship.dishes_rating.repository.jpa;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.jdbc.Sql;
import ru.bolshakov.internship.dishes_rating.data.jpa.TestRestaurantData;
import ru.bolshakov.internship.dishes_rating.model.jpa.Restaurant;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Sql("classpath:scripts/data.sql")
class JpaRestaurantRepositoryTest {
    TestRestaurantData data = new TestRestaurantData();

    @Autowired
    private JpaRestaurantRepository restaurantRepository;

    @BeforeEach
    void setUp() {
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
        Assertions.assertDoesNotThrow(() -> restaurantRepository.deleteById(data.existentRestaurantId));
    }

    @Test
    void deleteIfRestaurantDoesNotExist() {
        Assertions.assertThrows(EmptyResultDataAccessException.class, () -> restaurantRepository.deleteById(data.nonExistentRestaurantId));
    }

    @Test
    void get() {
        assertThat(restaurantRepository.findById(data.existentRestaurantId).orElseThrow())
                .isEqualToIgnoringGivenFields(data.TEST_RESTAURANT_1, "id", "menus", "votes");
    }

    @Test
    void getIfRestaurantDoesNotExist() {
        Assertions.assertEquals(Optional.empty(), restaurantRepository.findById(data.nonExistentRestaurantId));
    }

    @Test
    void getAllIfRequiredFirstPage() {
        restaurantRepository.save(new Restaurant("Restaurant4", "Some description for 4th restaurant"));
        Page<Restaurant> restaurantPage = restaurantRepository.findAll(PageRequest.of(0, 3));
        List<Restaurant> restaurants = restaurantPage.getContent();
        Assertions.assertEquals(3, restaurants.size());
        Assertions.assertEquals(data.TEST_RESTAURANT_1.getRestaurantName(), restaurants.get(0).getRestaurantName());
        Assertions.assertEquals(data.TEST_RESTAURANT_2.getRestaurantName(), restaurants.get(1).getRestaurantName());
        Assertions.assertEquals(data.TEST_RESTAURANT_3.getRestaurantName(), restaurants.get(2).getRestaurantName());
    }

    @Test
    void getAllIfRequiredSecondPage() {
        Restaurant restaurant4 = new Restaurant("Restaurant4", "Some description for 4th restaurant");
        restaurantRepository.save(restaurant4);
        Page<Restaurant> restaurantPage = restaurantRepository.findAll(PageRequest.of(1, 2));
        List<Restaurant> restaurants = restaurantPage.getContent();
        Assertions.assertEquals(2, restaurants.size());
        Assertions.assertEquals(data.TEST_RESTAURANT_3.getRestaurantName(), restaurants.get(0).getRestaurantName());
        Assertions.assertEquals(restaurant4.getRestaurantName(), restaurants.get(1).getRestaurantName());
    }

    @Test
    void getByName() {
        Restaurant returnedRestaurant = restaurantRepository.findByRestaurantName(data.TEST_RESTAURANT_1.getRestaurantName()).orElseThrow();
        Assertions.assertEquals(data.existentRestaurantId,returnedRestaurant.getId());
        Assertions.assertEquals(data.TEST_RESTAURANT_1.getRestaurantName(), returnedRestaurant.getRestaurantName());
        Assertions.assertEquals(data.TEST_RESTAURANT_1.getDescription(), returnedRestaurant.getDescription());
    }

    @Test
    void getByNameIfRestaurantDoesNotExist() {
        Assertions.assertEquals(Optional.empty(), restaurantRepository.findByRestaurantName("notExistentName"));

    }
}
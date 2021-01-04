package ru.bolshakov.internship.dishes_rating.repository.jdbc.menu;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import ru.bolshakov.internship.dishes_rating.data.jdbc.menu.TestMenuData;
import ru.bolshakov.internship.dishes_rating.model.jdbc.Menu;
import ru.bolshakov.internship.dishes_rating.repository.jdbc.JdbcMenuRepository;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Sql("classpath:scripts/data.sql")
class JdbcMenuRepositoryTest {
    private final TestMenuData data = new TestMenuData();

    @Autowired
    JdbcMenuRepository menuRepository;

    @Autowired
    JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        jdbcTemplate.update("DELETE FROM menu");
        jdbcTemplate.update("DELETE FROM restaurant");
        jdbcTemplate.update("INSERT INTO restaurant VALUES (1, 'restaurant1 name', 'description for restaurant1') ");
        jdbcTemplate.update("INSERT INTO restaurant VALUES (2, 'restaurant2 name', 'description for restaurant2') ");
        jdbcTemplate.update("INSERT INTO restaurant VALUES (3, 'restaurant3 name', 'description for restaurant3') ");
        jdbcTemplate.update("INSERT INTO restaurant VALUES (4, 'restaurant4 name', 'description for restaurant4') ");
        data.existentMenuId = menuRepository.save(data.TEST_MENU_1).getId();
        menuRepository.save(data.TEST_MENU_2);
        menuRepository.save(data.TEST_MENU_3);
    }

    @Test
    void saveNewMenu() {
        Menu newMenu = new Menu(LocalDate.now(), data.restaurantWithoutMenuId);
        Menu createdMenu = menuRepository.save(newMenu);
        assertNotNull(createdMenu.getId());
        assertEquals(newMenu.getMenuDate(), createdMenu.getMenuDate());
        assertEquals(newMenu.getRestaurantId(), createdMenu.getRestaurantId());
    }

    @Test
    void saveNewMenuIfRestaurantAlreadyHasMenuToAnotherDate() {
        Menu newMenu = new Menu(data.notToday, data.restaurantWithMenuId);
        Menu createdMenu = menuRepository.save(newMenu);
        assertNotNull(createdMenu.getId());
        assertEquals(newMenu.getMenuDate(), createdMenu.getMenuDate());
        assertEquals(newMenu.getRestaurantId(), createdMenu.getRestaurantId());
    }

    @Test
    void saveNewMenuIfRestaurantAlreadyHasMenuToThisDate() {
        Menu newMenu = new Menu(data.today, data.restaurantWithMenuId);
        assertThrows(DataIntegrityViolationException.class, () -> menuRepository.save(newMenu));
    }

    @Test
    void saveNewMenuIfRestaurantDoesNotExist() {
        Menu newMenu = new Menu(data.today, data.nonExistentRestaurantId);
        assertThrows(DataIntegrityViolationException.class, () -> menuRepository.save(newMenu));
    }

    @Test
    void update() {
        Menu menuToUpdate = new Menu(data.existentMenuId, LocalDate.of(1999, 12, 15), data.restaurantWithMenuId);
        Menu updatedMenu = menuRepository.save(menuToUpdate);
        assertEquals(menuToUpdate.getMenuDate(), updatedMenu.getMenuDate());
        assertEquals(menuToUpdate.getRestaurantId(), updatedMenu.getRestaurantId());
    }

    @Test
    void updateIfMenuDoesNotExist() {
        Menu returnedMenu = menuRepository.save(new Menu(data.nonExistentMenuId, LocalDate.of(1999, 12, 15), data.restaurantWithMenuId));
        assertNull(returnedMenu);
    }

    @Test
    void get() {
        Menu receivedMenu = menuRepository.get(data.existentMenuId);
        assertEquals(data.TEST_MENU_1.getMenuDate(), receivedMenu.getMenuDate());
        assertEquals(data.TEST_MENU_1.getRestaurantId(), receivedMenu.getRestaurantId());
    }

    @Test
    void getIfMenuDoesNotExist() {
        assertNull(menuRepository.get(data.nonExistentMenuId));
    }

    @Test
    void delete() {
        assertTrue(menuRepository.delete(data.existentMenuId));
        assertNull(menuRepository.get(data.existentMenuId));
        assertFalse(menuRepository.delete(data.existentMenuId));
    }

    @Test
    void deleteIfMenuDoesNotExist() {
        assertFalse(menuRepository.delete(data.nonExistentMenuId));
    }

    @Test
    void getAll() {
        List<Menu> menus = menuRepository.getAll();
        assertEquals(3, menus.size());
        assertEquals(data.TEST_MENU_1.getMenuDate(), menus.get(0).getMenuDate());
        assertEquals(data.TEST_MENU_1.getRestaurantId(), menus.get(0).getRestaurantId());

        assertEquals(data.TEST_MENU_2.getMenuDate(), menus.get(1).getMenuDate());
        assertEquals(data.TEST_MENU_2.getRestaurantId(), menus.get(1).getRestaurantId());

        assertEquals(data.TEST_MENU_3.getMenuDate(), menus.get(2).getMenuDate());
        assertEquals(data.TEST_MENU_3.getRestaurantId(), menus.get(2).getRestaurantId());
    }

    @Test
    void getByDateIfDateWithoutVotes() {
        List<Menu> menusByDate = menuRepository.getByRestaurant(data.restaurantWithoutMenuId);
        assertEquals(0, menusByDate.size());
    }

    @Test
    void getByCurrentDate() {
        List<Menu> votesBetweenDate = menuRepository.getByRestaurant(data.restaurantWithMenuId);
        assertEquals(1, votesBetweenDate.size());
    }

    @Test
    void getByCurrentDateForCertainUser() {
        assertNotNull(menuRepository.getByDateForCertainRestaurant(LocalDate.now(), data.restaurantWithMenuId));
    }

    @Test
    void getByCurrentDateForNonExistentUser() {
        assertNull(menuRepository.getByDateForCertainRestaurant(LocalDate.now(), data.nonExistentRestaurantId));
    }
}
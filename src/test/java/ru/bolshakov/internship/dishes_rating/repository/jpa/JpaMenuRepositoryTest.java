package ru.bolshakov.internship.dishes_rating.repository.jpa;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.jdbc.Sql;
import ru.bolshakov.internship.dishes_rating.data.jpa.TestMenuData;
import ru.bolshakov.internship.dishes_rating.model.jpa.Menu;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Sql({"classpath:scripts/data.sql", "classpath:scripts/populate_before_menu_repo_tests.sql"})
class JpaMenuRepositoryTest {
    private final TestMenuData data = new TestMenuData();

    @Autowired
    JpaMenuRepository menuRepository;

    @BeforeEach
    void setUp() {
        data.existentMenuId = menuRepository.save(data.TEST_MENU_1).getId();
        menuRepository.save(data.TEST_MENU_2);
        menuRepository.save(data.TEST_MENU_3);
    }

    @Test
    void saveNewMenu() {
        Menu newMenu = new Menu(LocalDate.now(), data.restaurantWithoutMenu);
        Menu createdMenu = menuRepository.save(newMenu);
        assertNotNull(createdMenu.getId());
        assertEquals(newMenu.getMenuDate(), createdMenu.getMenuDate());
        assertEquals(newMenu.getRestaurant(), createdMenu.getRestaurant());
    }

    @Test
    void saveNewMenuIfRestaurantAlreadyHasMenuToAnotherDate() {
        Menu newMenu = new Menu(data.notToday, data.restaurantWithMenu);
        Menu createdMenu = menuRepository.save(newMenu);
        assertNotNull(createdMenu.getId());
        assertEquals(newMenu.getMenuDate(), createdMenu.getMenuDate());
        assertEquals(newMenu.getRestaurant(), createdMenu.getRestaurant());
    }

    @Test
    void saveNewMenuIfRestaurantAlreadyHasMenuToThisDate() {
        Menu newMenu = new Menu(data.today, data.restaurantWithMenu);
        assertThrows(DataIntegrityViolationException.class, () -> menuRepository.save(newMenu));
    }

    @Test
    void saveNewMenuIfRestaurantDoesNotExist() {
        Menu newMenu = new Menu(data.today, data.nonExistentRestaurant);
        assertThrows(DataIntegrityViolationException.class, () -> menuRepository.save(newMenu));
    }

    @Test
    void update() {
        Menu menuToUpdate = new Menu(data.existentMenuId, LocalDate.of(1999, 12, 15), data.restaurantWithMenu);
        Menu updatedMenu = menuRepository.save(menuToUpdate);
        assertEquals(menuToUpdate.getMenuDate(), updatedMenu.getMenuDate());
    }

    @Test
    void get() {
        Menu receivedMenu = menuRepository.findById(data.existentMenuId).orElseThrow();
        assertEquals(data.TEST_MENU_1.getMenuDate(), receivedMenu.getMenuDate());
    }

    @Test
    void getIfMenuDoesNotExist() {
        assertEquals(Optional.empty(), menuRepository.findById(data.nonExistentMenuId));
    }

    @Test
    void delete() {
        assertDoesNotThrow(() -> menuRepository.deleteById(data.existentMenuId));
        assertEquals(Optional.empty(), menuRepository.findById(data.existentMenuId));
        assertThrows(EmptyResultDataAccessException.class, () -> menuRepository.deleteById(data.existentMenuId));
    }

    @Test
    void deleteIfMenuDoesNotExist() {
        assertThrows(EmptyResultDataAccessException.class, () -> menuRepository.deleteById(data.nonExistentMenuId));
    }

    @Test
    void getAll() {
        List<Menu> menus = menuRepository.findAll();
        assertEquals(3, menus.size());
        assertEquals(data.TEST_MENU_1.getMenuDate(), menus.get(0).getMenuDate());
        assertEquals(data.TEST_MENU_2.getMenuDate(), menus.get(1).getMenuDate());
        assertEquals(data.TEST_MENU_3.getMenuDate(), menus.get(2).getMenuDate());
    }

    @Test
    void getByDateIfDateWithoutVotes() {
        List<Menu> menusByDate = menuRepository.findByRestaurant_Id(data.restaurantWithoutMenu.getId());
        assertEquals(0, menusByDate.size());
    }

    @Test
    void getByCurrentDate() {
        List<Menu> votesBetweenDate = menuRepository.findByRestaurant_Id(data.restaurantWithMenu.getId());
        assertEquals(1, votesBetweenDate.size());
    }

    @Test
    void getByCurrentDateForCertainUser() {
        assertNotNull(menuRepository.findByRestaurant_IdAndMenuDate(data.restaurantWithMenu.getId(), LocalDate.now()));
    }

    @Test
    void getByCurrentDateForNonExistentUser() {
        assertEquals(Optional.empty(), menuRepository.findByRestaurant_IdAndMenuDate(data.nonExistentRestaurant.getId(), LocalDate.now()));
    }
}
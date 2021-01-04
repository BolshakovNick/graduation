package ru.bolshakov.internship.dishes_rating.repository.jdbc.user;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import ru.bolshakov.internship.dishes_rating.data.jdbc.user.TestUserData;
import ru.bolshakov.internship.dishes_rating.model.Role;
import ru.bolshakov.internship.dishes_rating.model.jdbc.User;
import ru.bolshakov.internship.dishes_rating.repository.jdbc.JdbcUserRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Sql("classpath:scripts/data.sql")
class JdbcUserRepositoryTest {

    private final TestUserData data = new TestUserData();

    @Autowired
    JdbcUserRepository userRepository;

    @Autowired
    JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        jdbcTemplate.update("DELETE FROM users");
        data.existentUserId = userRepository.save(data.TEST_USER_1).getId();
        userRepository.save(data.TEST_USER_2);
        userRepository.save(data.TEST_USER_3);
    }

    @Test
    void saveNewUser() {
        User createdUser = userRepository.save(data.NEW_USER);
        assertNotNull(createdUser.getId());
        Assertions.assertEquals(data.NEW_USER.getUserName(), createdUser.getUserName());
        Assertions.assertEquals(data.NEW_USER.getEmail(), createdUser.getEmail());
        Assertions.assertEquals(data.NEW_USER.getPassword(), createdUser.getPassword());
    }

    @Test
    void saveNewUserIfNameIsLongerThanConstraint() {
        User newUser = new User("qwertyuiopasdfghjklzxcvbnmqwertyuiopasdfghjklzxcvbnmm", "mail1", "password1");
        assertThrows(org.springframework.dao.DataIntegrityViolationException.class, () -> userRepository.save(newUser));
    }

    @Test
    void saveNewUserIfEmailIsLongerThanConstraint() {
        User newUser = new User("name1",
                "qwertyuiopasdfghjklzxcvbnmqwertyuiopasdfghjklzxcvbnmm" +
                        "qwertyuiopasdfghjklzxcvbnmqwertyuiopasdfghjklzxcvbnmm" +
                        "qwertyuiopasdfghjklzxcvbnmqwertyuiopasdfghjklzxcvbnmm" +
                        "qwertyuiopasdfghjklzxcvbnmqwertyuiopasdfghjklzxcvbnmm" +
                        "qwertyuiopasdfghjklzxcvbnmqwertyuiopasdfghjklzxcvbnmm",
                "password1",
                Role.USER);
        assertThrows(org.springframework.dao.DataIntegrityViolationException.class, () -> userRepository.save(newUser));
    }

    @Test
    void update () {
        User userToUpdate = new User(data.existentUserId, "updated name", "updated@email.dom", "updatedPassword");
        User updatedUser = userRepository.save(userToUpdate);
        Assertions.assertEquals(userToUpdate.getUserName(), updatedUser.getUserName());
        Assertions.assertEquals(userToUpdate.getEmail(), updatedUser.getEmail());
        Assertions.assertEquals(userToUpdate.getPassword(), updatedUser.getPassword());
        Assertions.assertEquals(userToUpdate.getRole(), updatedUser.getRole());
    }

    @Test
    void updateIfUserDoesNotExist() {
        User createdUser = userRepository.save(data.NON_EXISTENT_USER);
        Assertions.assertNull(createdUser);
    }

    @Test
    void get() {
        User receivedUser = userRepository.get(data.existentUserId);
        Assertions.assertEquals(data.TEST_USER_1.getUserName(), receivedUser.getUserName());
        Assertions.assertEquals(data.TEST_USER_1.getEmail(), receivedUser.getEmail());
        Assertions.assertEquals(data.TEST_USER_1.getPassword(), receivedUser.getPassword());
    }

    @Test
    void getIfUserDoesNotExist() {
        assertNull(userRepository.get(data.NON_EXISTENT_ID));
    }

    @Test
    void getByMail() {
        String userEmail = "mail1@host.domain";
        User receivedUser = userRepository.getByEmail(userEmail);
        Assertions.assertEquals(data.TEST_USER_1.getUserName(), receivedUser.getUserName());
        Assertions.assertEquals(data.TEST_USER_1.getEmail(), receivedUser.getEmail());
        Assertions.assertEquals(data.TEST_USER_1.getPassword(), receivedUser.getPassword());
    }

    @Test
    void getByEmailIfUserDoesNotExist() {
        String nonExistentEmail = "non-existent";
        assertNull(userRepository.getByEmail(nonExistentEmail));
    }

    @Test
    void delete() {
        User createdUser = userRepository.save(data.NEW_USER);
        assertTrue(userRepository.delete(createdUser.getId()));
        assertNull(userRepository.get(createdUser.getId()));
    }

    @Test
    void deleteIfUserDoesNotExist() {
        assertFalse(userRepository.delete(data.NON_EXISTENT_ID));
    }

    @Test
    void getAll() {
        List<User> users = userRepository.getAll();
        assertEquals(data.TEST_USERS, users);
    }
}
package ru.bolshakov.internship.dishes_rating.repository.jpa;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.jdbc.Sql;
import ru.bolshakov.internship.dishes_rating.data.jpa.TestUserData;
import ru.bolshakov.internship.dishes_rating.model.Role;
import ru.bolshakov.internship.dishes_rating.model.jpa.User;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Sql("classpath:scripts/data.sql")
class JpaUserRepositoryTest {

    private final TestUserData data = new TestUserData();

    @Autowired
    JpaUserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
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
    void update() {
        User userToUpdate = new User(data.existentUserId, "updated name", "updated@email.dom", "updatedPassword");
        User updatedUser = userRepository.save(userToUpdate);
        Assertions.assertEquals(userToUpdate.getUserName(), updatedUser.getUserName());
        Assertions.assertEquals(userToUpdate.getEmail(), updatedUser.getEmail());
        Assertions.assertEquals(userToUpdate.getPassword(), updatedUser.getPassword());
        Assertions.assertEquals(userToUpdate.getRole(), updatedUser.getRole());
    }

    @Test
    void get() {
        User receivedUser = userRepository.findById(data.existentUserId).orElseThrow();
        Assertions.assertEquals(data.TEST_USER_1.getUserName(), receivedUser.getUserName());
        Assertions.assertEquals(data.TEST_USER_1.getEmail(), receivedUser.getEmail());
        Assertions.assertEquals(data.TEST_USER_1.getPassword(), receivedUser.getPassword());
    }

    @Test
    void getIfUserDoesNotExist() {
        assertEquals(Optional.empty(), userRepository.findById(data.NON_EXISTENT_ID));
    }

    @Test
    void getByMail() {
        String userEmail = "mail1@host.domain";
        User receivedUser = userRepository.findByEmail(userEmail).orElseThrow();
        Assertions.assertEquals(data.TEST_USER_1.getUserName(), receivedUser.getUserName());
        Assertions.assertEquals(data.TEST_USER_1.getEmail(), receivedUser.getEmail());
        Assertions.assertEquals(data.TEST_USER_1.getPassword(), receivedUser.getPassword());
    }

    @Test
    void getByEmailIfUserDoesNotExist() {
        String nonExistentEmail = "non-existent";
        assertEquals(Optional.empty(), userRepository.findByEmail(nonExistentEmail));
    }

    @Test
    void delete() {
        User createdUser = userRepository.save(data.NEW_USER);
        assertDoesNotThrow(() -> userRepository.deleteById(createdUser.getId()));
        assertEquals(Optional.empty(), userRepository.findById(createdUser.getId()));
    }

    @Test
    void deleteIfUserDoesNotExist() {
        assertThrows(EmptyResultDataAccessException.class, () -> userRepository.deleteById(data.NON_EXISTENT_ID));
    }

    @Test
    void getAll() {
        List<User> users = userRepository.findAll();
        assertEquals(data.TEST_USERS, users);
    }

    @Test
    void getAllIfRequiredFirstPage() {
        userRepository.save(new User("name4", "mail4@host.domain", "password4"));
        Page<User> userPage = userRepository.findAll(PageRequest.of(0, 3));
        List<User> users = userPage.getContent();
        Assertions.assertEquals(3, users.size());
        Assertions.assertEquals(data.TEST_USER_1.getEmail(), users.get(0).getEmail());
        Assertions.assertEquals(data.TEST_USER_2.getEmail(), users.get(1).getEmail());
        Assertions.assertEquals(data.TEST_USER_3.getEmail(), users.get(2).getEmail());
    }

    @Test
    void getAllIfRequiredSecondPage() {
        User user4 = new User("name4", "mail4@host.domain", "password4");
        userRepository.save(user4);
        Page<User> userPage = userRepository.findAll(PageRequest.of(1, 2));
        List<User> users = userPage.getContent();
        Assertions.assertEquals(2, users.size());
        Assertions.assertEquals(data.TEST_USER_3.getEmail(), users.get(0).getEmail());
        Assertions.assertEquals(user4.getEmail(), users.get(1).getEmail());
    }
}
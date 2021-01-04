package ru.bolshakov.internship.dishes_rating.data.jdbc.user;

import ru.bolshakov.internship.dishes_rating.model.jdbc.User;

import java.util.Arrays;
import java.util.List;

public class TestUserData {
    public final User TEST_USER_1 = new User("name1", "mail1@host.domain", "password1");
    public final User TEST_USER_2 = new User("name2", "mail2@host.domain", "password2");
    public final User TEST_USER_3 = new User("name3", "mail3@host.domain", "password3");
    public final User NEW_USER = new User("newName", "newMail@host.domain", "newPassword");
    public final User NON_EXISTENT_USER = new User(9999L, "newName1", "newMail1@host.domain", "newPassword1");

    public Long existentUserId;

    public final long NON_EXISTENT_ID = 0;

    public final List<User> TEST_USERS = Arrays.asList(TEST_USER_1, TEST_USER_2, TEST_USER_3);
}
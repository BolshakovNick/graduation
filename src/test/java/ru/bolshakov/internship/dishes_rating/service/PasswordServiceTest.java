package ru.bolshakov.internship.dishes_rating.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class PasswordServiceTest {

    private final PasswordService service = new PasswordService();

    @Test
    void matches() {
        String password = "simplePassword";
        String encodedPassword = service.encode(password);
        Assertions.assertTrue(service.matches(password, encodedPassword));
    }

    @Test
    void matchesIfNotSame() {
        String encodedPassword = service.encode("simplePassword");
        String anotherPassword = "anotherPassword";
        Assertions.assertFalse(service.matches(anotherPassword, encodedPassword));
    }
}
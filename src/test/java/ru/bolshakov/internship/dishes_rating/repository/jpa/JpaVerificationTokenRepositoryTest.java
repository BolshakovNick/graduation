package ru.bolshakov.internship.dishes_rating.repository.jpa;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import ru.bolshakov.internship.dishes_rating.model.Role;
import ru.bolshakov.internship.dishes_rating.model.jpa.User;
import ru.bolshakov.internship.dishes_rating.model.jpa.VerificationToken;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@SpringBootTest
@Sql({"classpath:scripts/data.sql", "classpath:scripts/populate_before_token_repo_tests.sql"})
class JpaVerificationTokenRepositoryTest {

    @Autowired
    JpaVerificationTokenRepository repository;

    private String uuid;
    private VerificationToken verificationToken;

    @BeforeEach
    void setup() {
        uuid = "some_uuid";
        LocalDateTime expiryDate = LocalDateTime.now().plus(24, ChronoUnit.HOURS);
        User user = new User(1L, "name1", "email1@mail.com", "password1", Role.USER);
        user.setEnabled(true);
        verificationToken = new VerificationToken(uuid, user, expiryDate);
        repository.save(verificationToken);
    }

    @Test
    void findByUuid() {
        VerificationToken byUuid = repository.findByUuid(uuid).orElseThrow();
        Assertions.assertEquals(verificationToken.getId(), byUuid.getId());
        Assertions.assertEquals(verificationToken.getExpiryDate().truncatedTo(ChronoUnit.MILLIS), byUuid.getExpiryDate().truncatedTo(ChronoUnit.MILLIS));
        Assertions.assertEquals(verificationToken.getUuid(), byUuid.getUuid());
    }
}
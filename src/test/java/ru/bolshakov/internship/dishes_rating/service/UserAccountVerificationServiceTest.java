package ru.bolshakov.internship.dishes_rating.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.bolshakov.internship.dishes_rating.exception.TokenExpiredException;
import ru.bolshakov.internship.dishes_rating.model.jpa.User;
import ru.bolshakov.internship.dishes_rating.model.jpa.VerificationToken;
import ru.bolshakov.internship.dishes_rating.repository.jpa.JpaUserRepository;
import ru.bolshakov.internship.dishes_rating.repository.jpa.JpaVerificationTokenRepository;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
class UserAccountVerificationServiceTest {
    private static final User USER_BEFORE_ACTIVATION = new User("name1", "mail1@host.domain", "password1");

    @Mock
    private JpaVerificationTokenRepository tokenRepository;

    @Mock
    private JpaUserRepository userRepository;

    @Mock
    private MailMessageService messageService;

    @Mock
    private VerificationTokenService tokenService;

    @InjectMocks
    private UserAccountVerificationService accountActivationService;

    @Test
    void verify() {
        LocalDateTime expiryDate = LocalDateTime.now().plus(24, ChronoUnit.HOURS);

        User userAfterActivation = new User(123L, USER_BEFORE_ACTIVATION.getUserName(), USER_BEFORE_ACTIVATION.getEmail(), USER_BEFORE_ACTIVATION.getPassword(), USER_BEFORE_ACTIVATION.getRole());
        userAfterActivation.setEnabled(true);

        String uuid = UUID.randomUUID().toString();

        Mockito.when(tokenRepository.findByUuid(uuid))
                .thenReturn(Optional.of(new VerificationToken(1L, uuid, USER_BEFORE_ACTIVATION, expiryDate)));
        Mockito.when(userRepository.save(USER_BEFORE_ACTIVATION))
                .thenReturn(userAfterActivation);

        Assertions.assertDoesNotThrow(() -> accountActivationService.activateUserAccount(uuid));
    }

    @Test
    void verifyWhenTokenHasExpired() {
        LocalDateTime expiryDate = LocalDateTime.now();
        String uuid = UUID.randomUUID().toString();

        VerificationToken expiredToken = new VerificationToken(1L, uuid, USER_BEFORE_ACTIVATION, expiryDate);

        Mockito.when(tokenRepository.findByUuid(uuid))
                .thenReturn(Optional.of(expiredToken));
        Mockito.doNothing().when(tokenService).deleteById(expiredToken.getId());

        Assertions.assertThrows(TokenExpiredException.class, () -> accountActivationService.activateUserAccount(uuid));
    }
}
package ru.bolshakov.internship.dishes_rating.service;

import org.springframework.context.MessageSource;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.bolshakov.internship.dishes_rating.dto.user.AuthorizationRequestDTO;
import ru.bolshakov.internship.dishes_rating.exception.NotFoundException;
import ru.bolshakov.internship.dishes_rating.exception.TokenExpiredException;
import ru.bolshakov.internship.dishes_rating.model.User;
import ru.bolshakov.internship.dishes_rating.model.VerificationToken;
import ru.bolshakov.internship.dishes_rating.properties.VerificationProperties;
import ru.bolshakov.internship.dishes_rating.repository.jpa.JpaUserRepository;
import ru.bolshakov.internship.dishes_rating.repository.jpa.JpaVerificationTokenRepository;

import java.time.LocalDateTime;
import java.util.Locale;

@Service
public class UserAccountVerificationService {
    private final JpaVerificationTokenRepository tokenRepository;

    private final JpaUserRepository userRepository;

    private final MailMessageService messageService;

    private final VerificationTokenService tokenService;

    private final AuthenticationManager authenticationManager;

    private final VerificationProperties verificationProperties;

    private final MessageSource messageSource;

    public UserAccountVerificationService(JpaVerificationTokenRepository tokenRepository,
                                          JpaUserRepository userRepository,
                                          MailMessageService messageService,
                                          VerificationTokenService tokenService,
                                          AuthenticationManager authenticationManager,
                                          VerificationProperties verificationProperties,
                                          MessageSource messageSource) {
        this.tokenRepository = tokenRepository;
        this.userRepository = userRepository;
        this.messageService = messageService;
        this.tokenService = tokenService;
        this.authenticationManager = authenticationManager;
        this.verificationProperties = verificationProperties;
        this.messageSource = messageSource;
    }

    @Transactional
    public void activateUserAccount(String uuid) {
        VerificationToken token = tokenRepository.findByUuid(uuid)
                .orElseThrow(() -> new AccessDeniedException("Verification token not found"));
        User userByToken = token.getUser();
        if (token.getExpiryDate().compareTo(LocalDateTime.now()) <= 0) {
            tokenService.deleteById(token.getId());
            throw new TokenExpiredException("Token has expired");
        }
        tokenService.deleteById(token.getId());
        userByToken.setEnabled(true);
        userRepository.save(userByToken);
    }

    public void performUserVerification(AuthorizationRequestDTO requestDTO) {
        User userByEmail = userRepository.findByEmail(requestDTO.getEmail())
                .orElseThrow(() -> new NotFoundException("User with such email not found"));
        if (isUserEnabled(requestDTO)) {
            throw new AccessDeniedException("User account is has already activated");
        }
        deleteExistentToken(userByEmail);
        VerificationToken verificationTokenForUser = tokenService.createVerificationTokenForUser(userByEmail);
        messageService.sendLetterAsynchronously(userByEmail.getEmail(), verificationProperties.getMessageSubject(), getMessageText(verificationTokenForUser));
    }

    private boolean isUserEnabled(AuthorizationRequestDTO requestDTO) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(requestDTO.getEmail(), requestDTO.getPassword()));
            return true;
        } catch (DisabledException disabledException) {
            return false;
        }
    }

    private void deleteExistentToken(User user) {
        try {
            tokenService.deleteByUserId(user.getId());
        } catch (NotFoundException ignored) {
        }
    }

    private String getMessageText(VerificationToken verificationToken) {
        String message = messageSource.getMessage("registration.success.link", null, Locale.getDefault());
        String confirmationUrl = verificationProperties.getEndpointUrl() + verificationToken.getUuid();
        return String.format(verificationProperties.getMessagePattern(), message, confirmationUrl, confirmationUrl);
    }
}

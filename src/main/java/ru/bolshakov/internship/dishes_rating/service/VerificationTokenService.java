package ru.bolshakov.internship.dishes_rating.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.bolshakov.internship.dishes_rating.exception.NotFoundException;
import ru.bolshakov.internship.dishes_rating.model.jpa.User;
import ru.bolshakov.internship.dishes_rating.model.jpa.VerificationToken;
import ru.bolshakov.internship.dishes_rating.properties.VerificationProperties;
import ru.bolshakov.internship.dishes_rating.repository.jpa.JpaVerificationTokenRepository;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Service
public class VerificationTokenService {
    protected final Logger log = LoggerFactory.getLogger(VerificationTokenService.class);

    private final JpaVerificationTokenRepository tokenRepository;

    private final VerificationProperties properties;

    public VerificationTokenService(JpaVerificationTokenRepository tokenRepository, VerificationProperties properties) {
        this.tokenRepository = tokenRepository;
        this.properties = properties;
    }

    @Transactional
    public VerificationToken createVerificationTokenForUser(User user) {
        String uuid = UUID.randomUUID().toString();
        final VerificationToken myToken = new VerificationToken(uuid, user, calculateExpiryDate());
        return tokenRepository.save(myToken);
    }

    @Transactional
    public void deleteById(Long id) {
        try {
            tokenRepository.deleteById(id);
        } catch (
                EmptyResultDataAccessException e) {
            log.warn("Deleting {} failed. Possible reason: token with id {} does not exist in database", id, id);
            throw new NotFoundException("Token with such ID is not found");
        }
    }

    @Transactional
    public void deleteByUserId(Long id) {
        try {
            tokenRepository.deleteByUser_Id(id);
        } catch (EmptyResultDataAccessException e) {
            log.warn("Deleting {} failed. Possible reason: token with id {} does not exist in database", id, id);
            throw new NotFoundException("Token with such ID is not found");
        }
    }

    private LocalDateTime calculateExpiryDate() {
        return LocalDateTime.now().plus(properties.getTokenExpiration(), ChronoUnit.SECONDS);
    }
}

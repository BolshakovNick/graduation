package ru.bolshakov.internship.dishes_rating.service;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import ru.bolshakov.internship.dishes_rating.dto.AuthResponseDTO;
import ru.bolshakov.internship.dishes_rating.security.JwtTokenService;
import ru.bolshakov.internship.dishes_rating.security.SecurityUser;
import ru.bolshakov.internship.dishes_rating.security.TokenPair;

@Service
public class TokenRefreshingService {

    private final JwtTokenService tokenProvider;

    public TokenRefreshingService(JwtTokenService tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    public AuthResponseDTO refreshTokens(Authentication authentication) {
        SecurityUser details = (SecurityUser) authentication.getPrincipal();
        TokenPair tokenPair = tokenProvider.createToken(details.getUsername(), details.getUserId());
        return new AuthResponseDTO(tokenPair.getAccessToken(), tokenPair.getRefreshToken());
    }
}

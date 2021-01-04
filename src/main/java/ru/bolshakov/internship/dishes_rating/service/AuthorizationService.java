package ru.bolshakov.internship.dishes_rating.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;
import ru.bolshakov.internship.dishes_rating.dto.AuthResponseDTO;
import ru.bolshakov.internship.dishes_rating.dto.user.AuthorizationRequestDTO;
import ru.bolshakov.internship.dishes_rating.dto.user.UserDTO;
import ru.bolshakov.internship.dishes_rating.exception.AuthorizationFailureException;
import ru.bolshakov.internship.dishes_rating.exception.NotFoundException;
import ru.bolshakov.internship.dishes_rating.security.JwtTokenService;
import ru.bolshakov.internship.dishes_rating.security.TokenPair;

@Service
public class AuthorizationService {

    private final UserService service;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenService jwtTokenService;

    public AuthorizationService(UserService service, AuthenticationManager authenticationManager, JwtTokenService jwtTokenService) {
        this.service = service;
        this.authenticationManager = authenticationManager;
        this.jwtTokenService = jwtTokenService;
    }

    public AuthResponseDTO authorize(AuthorizationRequestDTO authorizationRequest) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authorizationRequest.getEmail(), authorizationRequest.getPassword()));
            UserDTO user = service.getByEmail(authorizationRequest.getEmail());
            TokenPair tokenPair = jwtTokenService.createToken(authorizationRequest.getEmail(), user.getId());
            return new AuthResponseDTO(tokenPair.getAccessToken(), tokenPair.getRefreshToken());
        } catch (AuthenticationException | NotFoundException e) {
            throw new AuthorizationFailureException("Authorization failure", e);
        }
    }
}

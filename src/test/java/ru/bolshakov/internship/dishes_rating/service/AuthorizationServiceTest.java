package ru.bolshakov.internship.dishes_rating.service;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import ru.bolshakov.internship.dishes_rating.dto.AuthResponseDTO;
import ru.bolshakov.internship.dishes_rating.dto.user.AuthorizationRequestDTO;
import ru.bolshakov.internship.dishes_rating.dto.user.UserDTO;
import ru.bolshakov.internship.dishes_rating.exception.AuthorizationFailureException;
import ru.bolshakov.internship.dishes_rating.exception.BadCredentialException;
import ru.bolshakov.internship.dishes_rating.exception.NotFoundException;
import ru.bolshakov.internship.dishes_rating.security.JwtTokenService;
import ru.bolshakov.internship.dishes_rating.security.TokenPair;

@ExtendWith(MockitoExtension.class)
class AuthorizationServiceTest {
    private static final TokenPair TOKEN_PAIR = new TokenPair("createdAccessToken", "createdRefreshToken");
    private static final AuthorizationRequestDTO AUTHORIZATION_REQUEST_DTO = new AuthorizationRequestDTO("mail1@mail.com", "password1");
    private static final UserDTO USER_DTO = new UserDTO(120L, "userName", AUTHORIZATION_REQUEST_DTO.getEmail(), AUTHORIZATION_REQUEST_DTO.getPassword(), "USER");

    @Mock
    private UserService userService;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtTokenService jwtTokenService;

    @InjectMocks
    AuthorizationService authorizationService;

    @Test
    void authorize() {
        Mockito.when(userService.getByEmail(AUTHORIZATION_REQUEST_DTO.getEmail())).thenReturn(USER_DTO);
        Mockito.when(jwtTokenService.createToken(AUTHORIZATION_REQUEST_DTO.getEmail(), USER_DTO.getId())).thenReturn(TOKEN_PAIR);

        AuthResponseDTO authorize = authorizationService.authorize(AUTHORIZATION_REQUEST_DTO);
        Assertions.assertEquals(TOKEN_PAIR.getAccessToken(), authorize.getAccessToken());
        Assertions.assertEquals(TOKEN_PAIR.getRefreshToken(), authorize.getRefreshToken());
    }

    @Test
    void authorizeIfUserIsDisabled() {
        Mockito.when(authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(AUTHORIZATION_REQUEST_DTO.getEmail(), AUTHORIZATION_REQUEST_DTO.getPassword())))
                .thenThrow(DisabledException.class);

        Assertions.assertThrows(AuthorizationFailureException.class, () -> authorizationService.authorize(AUTHORIZATION_REQUEST_DTO));
    }

    @Test
    void authorizeIfPasswordIsIncorrect() {
        Mockito.when(authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(AUTHORIZATION_REQUEST_DTO.getEmail(), AUTHORIZATION_REQUEST_DTO.getPassword())))
                .thenThrow(BadCredentialException.class);

        Assertions.assertThrows(AuthorizationFailureException.class, () -> authorizationService.authorize(AUTHORIZATION_REQUEST_DTO));
    }

    @Test
    void authorizeIfEmailDoesNotExist() {
        Mockito.when(authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(AUTHORIZATION_REQUEST_DTO.getEmail(), AUTHORIZATION_REQUEST_DTO.getPassword())))
                .thenThrow(NotFoundException.class);

        Assertions.assertThrows(AuthorizationFailureException.class, () -> authorizationService.authorize(AUTHORIZATION_REQUEST_DTO));
    }
}
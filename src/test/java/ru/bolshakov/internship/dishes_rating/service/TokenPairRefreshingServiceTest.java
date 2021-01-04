package ru.bolshakov.internship.dishes_rating.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import ru.bolshakov.internship.dishes_rating.dto.AuthResponseDTO;
import ru.bolshakov.internship.dishes_rating.security.JwtTokenService;
import ru.bolshakov.internship.dishes_rating.security.SecurityUser;
import ru.bolshakov.internship.dishes_rating.security.TokenPair;

@ExtendWith(MockitoExtension.class)
class TokenPairRefreshingServiceTest {

    @Mock
    private JwtTokenService jwtTokenService;

    @InjectMocks
    TokenRefreshingService service;

    @Mock
    Authentication authentication;

    @Test
    void refreshTokens() {
        String userName = "userName";
        String role = "USER";
        Long id = 123L;

        TokenPair tokenPair = new TokenPair("newAccessToken", "newRefreshToken");

        Mockito.when(authentication.getPrincipal()).thenReturn(new SecurityUser(id, userName, "password", role, true));
        Mockito.when(jwtTokenService.createToken(userName, id)).thenReturn(tokenPair);

        AuthResponseDTO auth = service.refreshTokens(authentication);
        Assertions.assertEquals(tokenPair.getAccessToken(), auth.getAccessToken());
        Assertions.assertEquals(tokenPair.getRefreshToken(), auth.getRefreshToken());
    }
}
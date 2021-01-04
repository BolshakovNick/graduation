package ru.bolshakov.internship.dishes_rating.controller.user;

import io.swagger.annotations.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.bolshakov.internship.dishes_rating.dto.AuthResponseDTO;
import ru.bolshakov.internship.dishes_rating.service.TokenRefreshingService;

@RestController
@RequestMapping("/authorization/token")
public class UserTokenRefreshingController {

    private final TokenRefreshingService service;

    public UserTokenRefreshingController(TokenRefreshingService service) {
        this.service = service;
    }

    @GetMapping("/refresh")
    @ApiResponse(code = 200, message = "Return refreshed tokens (access and refresh) for authorized user")
    public ResponseEntity<AuthResponseDTO> refreshToken(Authentication authentication) {
        return ResponseEntity.ok().body(service.refreshTokens(authentication));
    }
}

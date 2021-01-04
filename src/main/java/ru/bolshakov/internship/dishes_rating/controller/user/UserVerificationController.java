package ru.bolshakov.internship.dishes_rating.controller.user;

import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.bolshakov.internship.dishes_rating.dto.user.AuthorizationRequestDTO;
import ru.bolshakov.internship.dishes_rating.service.UserAccountVerificationService;

@RestController
@RequestMapping("/authorization")
public class UserVerificationController {

    private final UserAccountVerificationService service;

    public UserVerificationController(UserAccountVerificationService service) {
        this.service = service;
    }

    @PostMapping("/registration-confirm")
    @ResponseStatus(HttpStatus.OK)
    @ApiResponses({
            @ApiResponse(code = 200, message = "Get access user to his account"),
            @ApiResponse(code = 403, message = "Account is not verified")})
    public void confirm(@RequestParam(name = "uuid") @Parameter(name = "uuid", description = "Unique value (part of link) by which user can activate his account") String uuid) {
        service.activateUserAccount(uuid);
    }

    @PostMapping("/resend-confirm-letter")
    @ApiResponse(code = 200, message = "Send letter with new confirmation link to user email")
    public void resendConfirmLetter(@RequestBody AuthorizationRequestDTO requestDTO) {
        service.performUserVerification(requestDTO);
    }
}
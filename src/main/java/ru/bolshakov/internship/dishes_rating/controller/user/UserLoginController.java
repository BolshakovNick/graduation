package ru.bolshakov.internship.dishes_rating.controller.user;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.bolshakov.internship.dishes_rating.dto.AuthResponseDTO;
import ru.bolshakov.internship.dishes_rating.dto.user.AuthorizationRequestDTO;
import ru.bolshakov.internship.dishes_rating.dto.user.UserSavingRequestDTO;
import ru.bolshakov.internship.dishes_rating.service.AuthorizationService;
import ru.bolshakov.internship.dishes_rating.service.UserService;

import javax.validation.Valid;

@RestController
@RequestMapping("/authorization")
public class UserLoginController {

    private final UserService service;

    private final AuthorizationService authorizationService;

    public UserLoginController(UserService service, AuthorizationService authorizationService) {
        this.service = service;
        this.authorizationService = authorizationService;
    }

    @ApiOperation(value = "Allows the user to register if he entered valid data.")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Return ID which assigned to the registered User"),
            @ApiResponse(code = 400, message = "User entered invalid parameter(s)")})
    @PostMapping("/sign-up")
    public ResponseEntity<Long> register(@RequestBody @Valid UserSavingRequestDTO registrationRequest) {
        return ResponseEntity.ok(service.create(registrationRequest).getId());
    }

    @ApiOperation(value = "Allows the user to authorize if he entered valid data.")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Return ID of authorized User"),
            @ApiResponse(code = 400, message = "User entered invalid parameter(s)"),
            @ApiResponse(code = 401, message = "Unauthorized. Email or password is incorrect")
    })
    @PostMapping("/sign-in")
    public ResponseEntity<AuthResponseDTO> authorize(@RequestBody @Valid AuthorizationRequestDTO authorizationRequest) {
        return ResponseEntity.ok(authorizationService.authorize(authorizationRequest));
    }
}
package ru.bolshakov.internship.dishes_rating.controller.user;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ru.bolshakov.internship.dishes_rating.controller.AuthenticationUtil;
import ru.bolshakov.internship.dishes_rating.dto.user.UserSavingRequestDTO;
import ru.bolshakov.internship.dishes_rating.dto.user.UserDTO;
import ru.bolshakov.internship.dishes_rating.service.UserService;

import javax.validation.Valid;

@RestController
@RequestMapping("/profile")
public class UserProfileController {

    private final UserService service;

    public UserProfileController(UserService service) {
        this.service = service;
    }

    @ApiOperation(value = "Get information about user profile")
    @ApiResponse(code = 200, message = "Return profile of authenticated user")
    @GetMapping
    public ResponseEntity<UserDTO> getUserById(Authentication authentication) {
        return ResponseEntity.ok(service.get(AuthenticationUtil.getAuthUserId(authentication)));
    }

    @ApiOperation(value = "Update user profile")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Return ID of updated user"),
            @ApiResponse(code = 401, message = "User contains invalid parameters")})
    @PutMapping
    public ResponseEntity<Long> updateUser(Authentication authentication, @RequestBody @Valid UserSavingRequestDTO requestDTO) {
        return ResponseEntity.ok(service.update(AuthenticationUtil.getAuthUserId(authentication), requestDTO).getId());
    }

    @ApiOperation(value = "Delete user profile")
    @ApiResponse(code = 200, message = "Deleting was successful")
    @DeleteMapping
    @ResponseStatus(code = HttpStatus.OK)
    public void deleteUser(Authentication authentication) {
        service.delete(AuthenticationUtil.getAuthUserId(authentication));
    }
}

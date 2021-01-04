package ru.bolshakov.internship.dishes_rating.controller.user;

import io.swagger.annotations.*;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.bolshakov.internship.dishes_rating.dto.search.UserSearchRequest;
import ru.bolshakov.internship.dishes_rating.dto.user.UpdatingRequestDTO;
import ru.bolshakov.internship.dishes_rating.dto.user.UserDTO;
import ru.bolshakov.internship.dishes_rating.service.UserService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserManagementController {

    private final UserService service;

    public UserManagementController(UserService service) {
        this.service = service;
    }

    @ApiOperation(value = "Get list of all registered users")
    @ApiResponse(code = 200, message = "Return all users")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNumber", dataType = "integer", paramType = "query",
                    value = "Results page you want to retrieve (0...N)."),
            @ApiImplicitParam(name = "pageSize", dataType = "integer", paramType = "query",
                    value = "Number of records per page. MAX value is defined in configuration. " +
                            "If the request contains number greater than MAX value, " +
                            "it will be shown only as many elements per page, as allowed"),
            @ApiImplicitParam(name = "sort", allowMultiple = true, dataType = "string", paramType = "query",
                    value = "Sorting criteria in the format: property(,asc|desc). " +
                            "Default sort order is ascending. " +
                            "Multiple sort criteria are supported."),
            @ApiImplicitParam(name = "parameter", dataType = "string", paramType = "query",
                    value = "Parameter by which admin can find users by userName"),
            @ApiImplicitParam(name = "startWith", dataType = "boolean", paramType = "query",
                    value = "If parameter is a part with which userName starts"),
            @ApiImplicitParam(name = "endWith", dataType = "boolean", paramType = "query",
                    value = "If parameter is a part with which userName ends")
    })
    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUsers(Pageable pageable, UserSearchRequest request) {
        return ResponseEntity.ok(service.getAll(pageable, request));
    }

    @ApiOperation(value = "Get user by ID")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Return user by ID"),
            @ApiResponse(code = 404, message = "User with such id not found")})
    @GetMapping("/{userId}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable @Parameter(in = ParameterIn.PATH, description = "ID of existing user") Long userId) {
        return ResponseEntity.ok(service.get(userId));
    }

    @ApiOperation(value = "Update user by ID")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Return ID of updated user"),
            @ApiResponse(code = 401, message = "User contains invalid parameters"),
            @ApiResponse(code = 404, message = "User with such id not found")})
    @PutMapping("/{userId}")
    public ResponseEntity<Long> updateUser(@PathVariable @Parameter(in = ParameterIn.PATH, description = "ID of existing user") Long userId,
                                           @RequestBody @Valid UpdatingRequestDTO requestDTO) {
        return ResponseEntity.ok(service.update(userId, requestDTO).getId());
    }

    @ApiOperation(value = "Delete user by ID")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Deleting was successful"),
            @ApiResponse(code = 404, message = "User with such id not found")})
    @DeleteMapping("/{userId}")
    @ResponseStatus(code = HttpStatus.OK)
    public void deleteUser(@PathVariable @Parameter(in = ParameterIn.PATH, description = "ID of existing user") Long userId) {
        service.delete(userId);
    }
}

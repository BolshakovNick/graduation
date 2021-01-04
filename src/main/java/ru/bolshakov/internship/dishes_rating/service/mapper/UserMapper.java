package ru.bolshakov.internship.dishes_rating.service.mapper;

import org.springframework.stereotype.Component;
import ru.bolshakov.internship.dishes_rating.dto.user.*;
import ru.bolshakov.internship.dishes_rating.model.Role;
import ru.bolshakov.internship.dishes_rating.model.jpa.User;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserMapper {
    private static final String ROLE_USER = "USER";

    public UserDTO toDTO(User user) {
        return new UserDTO(user.getId(), user.getUserName(), user.getEmail(), user.getPassword(), user.getRole().name());
    }

    public List<UserDTO> toDTOs(List<User> users) {
        return users.stream().map(this::toDTO).collect(Collectors.toList());
    }

    public User toEntity(UserSavingRequestDTO requestDTO) {
        return new User(null, requestDTO.getUserName(), requestDTO.getEmail(), null, Role.valueOf(ROLE_USER));
    }

    public User toEntity(UserDTO userDTO) {
        return new User(userDTO.getId(), userDTO.getUserName(), userDTO.getEmail(), null, Role.valueOf(userDTO.getRole()));
    }

    public AuthorizationRequestDTO toAuthRequest(UserSavingRequestDTO request) {
        return new AuthorizationRequestDTO(request.getEmail(), request.getPassword());
    }

    public UpdatingRequestDTO toUpdateRequest(SavingRequestDTO requestDTO) {
        return new UpdatingRequestDTO(requestDTO.getUserName(), requestDTO.getEmail(), requestDTO.getPassword(), Role.USER);
    }
}

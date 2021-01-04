package ru.bolshakov.internship.dishes_rating.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import ru.bolshakov.internship.dishes_rating.model.Role;

public class UpdatingRequestDTO extends SavingRequestDTO {

    @Schema(description = "User role, which determines which endpoints the user has access to")

    private Role role;

    public UpdatingRequestDTO(String userName, String email, String password, Role role) {
        super(userName, email, password);
        this.role = role;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}

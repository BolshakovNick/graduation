package ru.bolshakov.internship.dishes_rating.dto.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
import java.util.Objects;

public class UserDTO {

    @Schema(description = "ID of this User", example = "1")
    @PositiveOrZero
    private Long id;

    @Schema(description = "Name of this User", example = "Firstname Lastname")
    @Size(min = 5, max = 50)
    @NotBlank
    private String userName;

    @Schema(description = "Email that belongs to this User", example = "mail@mail.com")
    @Email
    @Size(max = 256)
    @NotBlank
    private String email;

    @Schema(description = "User account password", example = "Password_123")
    @Size(min = 8, max = 50)
    @NotBlank
    @JsonIgnore
    private String password;

    @Schema(description = "The Role the user has (USER or ADMIN)", example = "USER")
    private String role;

    public UserDTO() {
    }

    public UserDTO(String userName, String email, String password, String role) {
        this(null, userName, email, password, role);
    }

    public UserDTO(Long id, String userName, String email, String password, String role) {
        this.id = id;
        this.userName = userName;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public Long getId() {
        return id;
    }

    public String getUserName() {
        return userName;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getRole() {
        return role;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserDTO user = (UserDTO) o;
        return Objects.equals(id, user.id) &&
                userName.equals(user.userName) &&
                email.equals(user.email) &&
                password.equals(user.password) &&
                role.equals(user.role);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userName, email, password, role);
    }

    @Override
    public String toString() {
        return "UserTo{" +
                "id=" + id +
                ", userName='" + userName + '\'' +
                ", email='" + email + '\'' +
                ", role='" + role + '\'' +
                '}';
    }
}

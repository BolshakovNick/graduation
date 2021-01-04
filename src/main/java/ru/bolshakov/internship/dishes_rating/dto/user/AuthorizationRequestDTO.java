package ru.bolshakov.internship.dishes_rating.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Objects;

public class AuthorizationRequestDTO {

    @Schema(description = "Email that belongs to this User", example = "mail@mail.com")
    @Email
    @Size(max = 256)
    @NotBlank
    private String email;

    @Schema(description = "User account password", example = "Password_123")
    @Size(min = 8, max = 50)
    @NotBlank
    private String password;

    public AuthorizationRequestDTO() {
    }

    public AuthorizationRequestDTO(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AuthorizationRequestDTO that = (AuthorizationRequestDTO) o;
        return email.equals(that.email) &&
                password.equals(that.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email, password);
    }

    @Override
    public String toString() {
        return "AuthorizationRequestDTO{" +
                "email='" + email + '\'' +
                '}';
    }
}

package ru.bolshakov.internship.dishes_rating.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Objects;

public abstract class SavingRequestDTO {
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
    private String password;

    public SavingRequestDTO(String userName, String email, String password) {
        this.userName = userName;
        this.email = email;
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SavingRequestDTO that = (SavingRequestDTO) o;
        return userName.equals(that.userName) &&
                email.equals(that.email) &&
                password.equals(that.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userName, email, password);
    }

    @Override
    public String toString() {
        return "UserSavingRequestDTO{" +
                "userName='" + userName + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}

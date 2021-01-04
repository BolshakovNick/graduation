package ru.bolshakov.internship.dishes_rating.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Objects;

public class AuthResponseDTO {
    @Schema(description = "Access token")
    private final String accessToken;

    @Schema(description = "Refresh token")
    private final String refreshToken;

    public AuthResponseDTO(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AuthResponseDTO that = (AuthResponseDTO) o;
        return accessToken.equals(that.accessToken) &&
                refreshToken.equals(that.refreshToken);
    }

    @Override
    public int hashCode() {
        return Objects.hash(accessToken, refreshToken);
    }

    @Override
    public String toString() {
        return "RefreshedTokenResponseDTO{" +
                "accessToken='" + accessToken + '\'' +
                ", refreshToken='" + refreshToken + '\'' +
                '}';
    }
}

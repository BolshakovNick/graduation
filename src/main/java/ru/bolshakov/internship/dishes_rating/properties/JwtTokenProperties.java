package ru.bolshakov.internship.dishes_rating.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "jwt")
public class JwtTokenProperties {
    private String accessSecret;
    private Long accessExpiration;

    private String refreshSecret;
    private Long refreshExpiration;

    public String getAccessSecret() {
        return accessSecret;
    }

    public void setAccessSecret(String accessSecret) {
        this.accessSecret = accessSecret;
    }

    public Long getAccessExpiration() {
        return accessExpiration;
    }

    public void setAccessExpiration(Long accessExpiration) {
        this.accessExpiration = accessExpiration;
    }

    public String getRefreshSecret() {
        return refreshSecret;
    }

    public void setRefreshSecret(String refreshSecret) {
        this.refreshSecret = refreshSecret;
    }

    public Long getRefreshExpiration() {
        return refreshExpiration;
    }

    public void setRefreshExpiration(Long refreshExpiration) {
        this.refreshExpiration = refreshExpiration;
    }
}

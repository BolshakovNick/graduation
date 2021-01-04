package ru.bolshakov.internship.dishes_rating.controller;

import org.springframework.security.core.Authentication;
import ru.bolshakov.internship.dishes_rating.security.SecurityUser;

public class AuthenticationUtil {

    public static Long getAuthUserId(Authentication authentication) {
        return ((SecurityUser) authentication.getPrincipal()).getUserId();
    }
}

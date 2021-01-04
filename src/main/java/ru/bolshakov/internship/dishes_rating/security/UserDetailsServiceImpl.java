package ru.bolshakov.internship.dishes_rating.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import ru.bolshakov.internship.dishes_rating.exception.NotFoundException;
import ru.bolshakov.internship.dishes_rating.model.jpa.User;
import ru.bolshakov.internship.dishes_rating.repository.jpa.JpaUserRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final JpaUserRepository repository;

    public UserDetailsServiceImpl(JpaUserRepository repository) {
        this.repository = repository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) {
        User user = repository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("User with such email not found"));
        return new SecurityUser(user);
    }
}

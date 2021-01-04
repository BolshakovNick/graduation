package ru.bolshakov.internship.dishes_rating.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import ru.bolshakov.internship.dishes_rating.model.Role;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final JwtConfigurer jwtConfigurer;

    public SecurityConfig(JwtConfigurer jwtConfigurer) {
        this.jwtConfigurer = jwtConfigurer;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers("/").permitAll()
                .antMatchers("/authorization/**").permitAll()
                .antMatchers("/swagger-ui/").hasRole(Role.ADMIN.name())
                .antMatchers("/restaurants/vote").hasRole(Role.USER.name())
                .antMatchers("/profile/**").hasAnyRole(Role.ADMIN.name(), Role.USER.name())
                .antMatchers("/users/**").hasRole(Role.ADMIN.name())
                .antMatchers(HttpMethod.GET,"/restaurants/**").hasAnyRole(Role.ADMIN.name(), Role.USER.name())
                .antMatchers(HttpMethod.POST,"/restaurants").hasRole(Role.ADMIN.name())
                .antMatchers(HttpMethod.PUT,"/restaurants/*").hasRole(Role.ADMIN.name())
                .antMatchers(HttpMethod.DELETE,"/restaurants/*").hasRole(Role.ADMIN.name())
                .antMatchers(HttpMethod.GET, "/restaurants/*/menu/**").hasAnyRole(Role.ADMIN.name(), Role.USER.name())
                .antMatchers(HttpMethod.POST,"/restaurants/*/menu/dishes").hasRole(Role.ADMIN.name())
                .antMatchers(HttpMethod.PUT,"/restaurants/*/menu/dishes/*").hasRole(Role.ADMIN.name())
                .antMatchers(HttpMethod.DELETE,"/restaurants/*/menu/dishes/*").hasRole(Role.ADMIN.name())


                .anyRequest()
                .authenticated()
                .and()
                .apply(jwtConfigurer);
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}

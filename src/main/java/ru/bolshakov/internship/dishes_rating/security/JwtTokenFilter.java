package ru.bolshakov.internship.dishes_rating.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;
import ru.bolshakov.internship.dishes_rating.exception.AuthorizationFailureException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtTokenFilter extends GenericFilterBean {
    private final JwtTokenService jwtTokenService;

    public JwtTokenFilter(JwtTokenService jwtTokenService) {
        this.jwtTokenService = jwtTokenService;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        String accessToken = jwtTokenService.resolveAccessToken((HttpServletRequest) servletRequest);
        try {
            if (accessToken != null) {
                jwtTokenService.validateToken(accessToken);
                Authentication authentication = jwtTokenService.getAuthentication(accessToken);
                if (authentication != null) {
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        } catch (AuthorizationFailureException e) {
            ((HttpServletResponse) servletResponse).sendError(401, e.getMessage());
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }
}
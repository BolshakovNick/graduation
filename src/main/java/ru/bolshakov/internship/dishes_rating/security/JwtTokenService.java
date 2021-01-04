package ru.bolshakov.internship.dishes_rating.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import ru.bolshakov.internship.dishes_rating.exception.AuthorizationFailureException;
import ru.bolshakov.internship.dishes_rating.properties.JwtTokenProperties;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtTokenService {
    private final UserDetailsServiceImpl userDetailsService;

    private static final String HEADER = "Authorization";

    private String accessSecretKey;

    private String refreshSecretKey;

    private final JwtTokenProperties properties;

    public JwtTokenService(UserDetailsServiceImpl userDetailsService, JwtTokenProperties properties) {
        this.userDetailsService = userDetailsService;
        this.properties = properties;
    }

    @PostConstruct
    protected void init() {
        accessSecretKey = Base64.getEncoder().encodeToString(properties.getAccessSecret().getBytes());
        refreshSecretKey = Base64.getEncoder().encodeToString(properties.getRefreshSecret().getBytes());
    }

    public TokenPair createToken(String username, Long id) {
        Claims claims = Jwts.claims().setSubject(username);
        claims.put("id", id);
        Date now = new Date();

        return new TokenPair(
                createToken(claims, now, properties.getAccessExpiration(), accessSecretKey),
                createToken(claims, now, properties.getRefreshExpiration(), refreshSecretKey));
    }

    public void validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(accessSecretKey).parseClaimsJws(token);
        } catch (RuntimeException e) {
            throw new AuthorizationFailureException("Token is expired or invalid", e);
        }
    }

    public Authentication getAuthentication(String accessToken) {
        UserDetails userDetails = this.userDetailsService.loadUserByUsername(getUsername(accessToken));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    public String getUsername(TokenPair tokenPair) {
        return getUsername(tokenPair.getAccessToken());
    }

    public String getUsername(String accessToken) {
        return Jwts.parser().setSigningKey(accessSecretKey).parseClaimsJws(accessToken).getBody().getSubject();
    }

    public Long getUserId(TokenPair tokenPair) {
        return Jwts.parser().setSigningKey(accessSecretKey).parseClaimsJws(tokenPair.getAccessToken()).getBody().get("id", Long.class);
    }

    public String resolveAccessToken(HttpServletRequest request) {
        return request.getHeader(HEADER);
    }

    private String createToken(Claims claims, Date now, Long expiration, String secretKey) {
        Date validity = new Date(new Date().getTime() + expiration * 1000);
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }
}

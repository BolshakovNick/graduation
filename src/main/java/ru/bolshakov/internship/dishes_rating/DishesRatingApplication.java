package ru.bolshakov.internship.dishes_rating;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import ru.bolshakov.internship.dishes_rating.properties.JwtTokenProperties;
import ru.bolshakov.internship.dishes_rating.properties.VerificationProperties;
import ru.bolshakov.internship.dishes_rating.properties.VotingProperties;

@SpringBootApplication
@EnableConfigurationProperties({
        VotingProperties.class,
        JwtTokenProperties.class,
        VerificationProperties.class
})
public class DishesRatingApplication {

    public static void main(String[] args) {
        SpringApplication.run(DishesRatingApplication.class, args);
    }
}

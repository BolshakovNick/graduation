package ru.bolshakov.internship.dishes_rating.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;
import java.time.LocalTime;

@ConfigurationProperties(prefix = "voting")
public class VotingProperties {
    private LocalTime boundaryTime;

    public LocalTime getBoundaryTime() {
        return boundaryTime;
    }

    public void setAllowedSecondsToVoteFromStartOfDay(Duration allowedSecondsToVoteFromStartOfDay) {
        this.boundaryTime = LocalTime.ofSecondOfDay(allowedSecondsToVoteFromStartOfDay.getSeconds());
    }
}

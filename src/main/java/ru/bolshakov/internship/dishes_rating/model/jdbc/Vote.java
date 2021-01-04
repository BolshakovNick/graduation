package ru.bolshakov.internship.dishes_rating.model.jdbc;

import java.time.LocalDateTime;

public class Vote extends BaseEntity {
    private final LocalDateTime votingDateTime;

    private final Long userId;

    private Long restaurantId;

    public Vote(Long userId, LocalDateTime votingDateTime, Long restaurantId) {
        this(null, userId, votingDateTime, restaurantId);
    }

    public Vote(Long id, Long userId, LocalDateTime votingDateTime, Long restaurantId) {
        super(id);
        this.votingDateTime = votingDateTime;
        this.userId = userId;
        this.restaurantId = restaurantId;
    }

    public LocalDateTime getVotingDateTime() {
        return votingDateTime;
    }

    public Long getUserId() {
        return userId;
    }

    public Long getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(Long restaurantId) {
        this.restaurantId = restaurantId;
    }

    @Override
    public String toString() {
        return "Vote{" +
                "id=" + super.id +
                "votingDateTime=" + votingDateTime +
                ", userId=" + userId +
                ", restaurantId=" + restaurantId +
                '}';
    }
}

package ru.bolshakov.internship.dishes_rating.model;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "vote")
public class Vote {

    @Id
    @SequenceGenerator(name = "vote_id_seq", sequenceName = "vote_id_seq", allocationSize = 1, initialValue = 100000)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "vote_id_seq")
    private Long id;

    @Column(name = "voting_datetime", nullable = false)
    private LocalDateTime votingDateTime;

    @JoinColumn(name = "user_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @JoinColumn(name = "restaurant_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Restaurant restaurant;

    public Vote() {
    }

    public Vote(User user, LocalDateTime votingDateTime, Restaurant restaurant) {
        this(null, user, votingDateTime, restaurant);
    }

    public Vote(Long id, User user, LocalDateTime votingDateTime, Restaurant restaurant) {
        this.id = id;
        this.votingDateTime = votingDateTime;
        this.user = user;
        this.restaurant = restaurant;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getVotingDateTime() {
        return votingDateTime;
    }

    public User getUser() {
        return user;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    public boolean isNew() {
        return this.id == null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vote vote = (Vote) o;
        return Objects.equals(id, vote.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Vote{" +
                "id=" + id +
                "votingDateTime=" + votingDateTime +
                '}';
    }
}

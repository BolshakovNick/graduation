package ru.bolshakov.internship.dishes_rating.model.jpa;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "verification_token")
@NamedEntityGraph(name = "VerificationToken.user", attributeNodes = @NamedAttributeNode("user"))
public class VerificationToken {
    @Id
    @SequenceGenerator(name = "verification_token_id_seq", sequenceName = "verification_token_id_seq", allocationSize = 1, initialValue = 100000)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "verification_token_id_seq")
    private Long id;

    @Column(name = "uuid", nullable = false, unique = true)
    private String uuid;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "expiry_date", nullable = false)
    private LocalDateTime expiryDate;

    public VerificationToken() {
    }

    public VerificationToken(String uuid, User user, LocalDateTime expiryDate) {
        this(null, uuid, user, expiryDate);
    }

    public VerificationToken(Long id, String uuid, User user, LocalDateTime expiryDate) {
        this.id = id;
        this.uuid = uuid;
        this.user = user;
        this.expiryDate = expiryDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String token) {
        this.uuid = token;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LocalDateTime getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(LocalDateTime expiryDate) {
        this.expiryDate = expiryDate;
    }
}

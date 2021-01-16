package ru.bolshakov.internship.dishes_rating.repository.jpa;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.bolshakov.internship.dishes_rating.model.VerificationToken;

import java.util.Optional;

@Repository
public interface JpaVerificationTokenRepository extends JpaRepository<VerificationToken, Long> {

    @EntityGraph(value = "VerificationToken.user")
    Optional<VerificationToken> findByUuid(String uuid);

    void deleteByUser_Id(Long userId);
}

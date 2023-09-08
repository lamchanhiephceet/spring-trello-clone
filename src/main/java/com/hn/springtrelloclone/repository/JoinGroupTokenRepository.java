package com.hn.springtrelloclone.repository;

import com.hn.springtrelloclone.model.JoinGroupToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JoinGroupTokenRepository extends JpaRepository<JoinGroupToken, Long> {
    Optional<JoinGroupToken> findByToken(String token);
}

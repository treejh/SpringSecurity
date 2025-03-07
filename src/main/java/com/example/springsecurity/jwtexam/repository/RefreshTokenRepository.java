package com.example.springsecurity.jwtexam.repository;

import com.example.springsecurity.jwtexam.domain.RefreshToken;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken,Long> {
    Optional<RefreshToken> findByValue(String value);
}

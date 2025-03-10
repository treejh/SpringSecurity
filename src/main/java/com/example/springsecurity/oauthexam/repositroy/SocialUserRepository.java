package com.example.springsecurity.oauthexam.repositroy;

import com.example.springsecurity.oauthexam.entity.SocialUser;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;


public interface SocialUserRepository extends JpaRepository<SocialUser, Long> {
    Optional<SocialUser> findBySocialIdAndProvider(String socialId, String provider);
}
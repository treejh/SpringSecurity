package com.example.springsecurity.oauthexam.repositroy;

import com.example.springsecurity.oauthexam.entity.SocialLoginInfo;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SocialLoginInfoRepository extends JpaRepository<SocialLoginInfo,Long> {
    Optional<SocialLoginInfo> findByProviderAndUuidAndSocialId(String provider, String uuid, String socialId);
}
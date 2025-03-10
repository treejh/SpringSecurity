package com.example.springsecurity.oauthexam.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "social_login_info")
@Getter
@Setter
public class SocialLoginInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String provider;
    private String socialId;
    private LocalDateTime createAt;
    private String uuid;

    public SocialLoginInfo(){
        //소셜 로그인 이후에 특정한 시간까지만 추가 작업이 가능 하게 하려고!!
        this.createAt = LocalDateTime.now();
        this.uuid = UUID.randomUUID().toString();
    }
}

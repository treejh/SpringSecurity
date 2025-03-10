package com.example.springsecurity.oauthexam.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.Getter;

@Entity
@Data
//소셜에서 로그인 했을때, 소셜이 가지고 있는 정보를 요청할때 사용
public class SocialUser {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String socialId;
    private String provider;
    private String username;
    private String email;
    private String avatarUrl;



}

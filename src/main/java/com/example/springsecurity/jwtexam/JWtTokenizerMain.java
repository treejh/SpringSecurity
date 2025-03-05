package com.example.springsecurity.jwtexam;


import com.example.springsecurity.jwtexam.jwtutil.JwtTokenizer;
import java.util.Arrays;

public class JWtTokenizerMain {
    public static void main(String[] args) {
        JwtTokenizer jwtTokenizer = new JwtTokenizer("12345678901234567890123456789012","12345678901234567890123456789012");
        String accessToken = jwtTokenizer.createAccessToken(1L,"test@test.com","test","testUser", Arrays.asList("ROLE_USER"));
        System.out.println(accessToken);

    }
}

package com.example.springsecurity.jwtexam.jwtutil;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


@Component
public class JwtTokenizer {

    private final byte [] accessSecret;
    private final byte [] refreshSecret;

    //토큰 만료 시간이 달라지면 안되기 때문에 static으로 사용해야 한다.
    public static final Long ACCESS_TOKEN_EXPIRE_COUNT = 30*60*1000L; //access Token 유효시간 30분
    public static final Long REFRESH_TOKEN_EXPIRE_COUNT = 60*60*1000L; //access Token 유효시간 1시간
    //7일 하고 싶으면  -> 7*24*60*60*1000L


    //야물 파일에 있는 데이터를 가지고 온다.
    public JwtTokenizer(@Value("{jwt.secretKey}") String accessSecret, @Value("{jwt.refreshKey}") String refreshSecret) {
        this.accessSecret = accessSecret.getBytes(StandardCharsets.UTF_8);
        this.refreshSecret = refreshSecret.getBytes(StandardCharsets.UTF_8);
    }

    //토큰은 인증에 대한 정보만 들어가게 하고, 비밀번호는 넣지 않는다(보안 문제 상 ) 불필요한 애들은 빼도 괜춘 ㅇㅇ
    private String createToken(Long id, String email, String name, String username, List<String> roles,
                               Long expire, byte[] secretKey){
       //필요한 정보들을 저장한다.
        //고유한 식별자 값이 subject에 들어가는게 좋다 (이메일 중복 안될테니 걍 이메일 넣은거임)
        Claims claims = Jwts.claims()
                .setSubject(email);

        //토큰을 만들어서 클라이언트한테 보낼때 포함될 값들을 저장하고 있는걱임ㅇ ㅇㅇ
        //클레임 종류
        //1. jwt가 가지고 있는 claim
        //2. jwt가 가지고 있지 않은 정보
        claims.put("username", username);
        claims.put("name",name);
        claims.put("userId",id);
        claims.put("roles",roles);


        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime() + expire))//만료시간 : , 현재 시간 + expire 더한값 -> 언제까지 사용할지
                .signWith(getSigningKey(secretKey))
                .compact();


    }

    private static Key getSigningKey(byte[] secretKey){
        return Keys.hmacShaKeyFor(secretKey);
    }

    public String createAccessToken(Long id, String email, String name, String username, List<String> roles){
        return createToken(id,email,name,username,roles, ACCESS_TOKEN_EXPIRE_COUNT,accessSecret);
    }

    public String createRefreshToken(Long id, String email, String name, String username, List<String> roles){
        return createToken(id,email,name,username,roles,REFRESH_TOKEN_EXPIRE_COUNT,refreshSecret);
    }

    //받은 토큰에서 데이터 받는 메서드
    public Claims parseToken(String token, byte[] secretKey){
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey(secretKey))
                .build()
                .parseClaimsJwt(token)
                .getBody();
    }


    public Long getUserIdFromToken(String token){
        //Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0QHRlc3QuY29tIiwidXNlcm5hbWUiOiJ0ZXN0dXNlciIsIm5hbWUiOiJ0ZXN0IiwidXNlcklkIjoxLCJyb2xlcyI6WyJST0xFX1VTRVIiXSwiaWF0IjoxNzQxMjIyMjIzLCJleHAiOjE3NDEyMjQwMjN9.Mw2TmisHqjWyECxjRbSYMvja2L41r1-_7m4IllBLsS4
        if(token == null || token.isBlank()){
            throw new IllegalArgumentException("JWT 토큰이 없습니다.");
        }

        if(!token.startsWith("Bearer ")){
            throw new IllegalArgumentException("유효하지 않은 형식입니다.");
        }
        Claims claims = parseToken(token, accessSecret);
        if(claims == null){
            throw new IllegalArgumentException("유효하지 않은 형식입니다.");
        }
        Object userId = claims.get("userId");
        if(userId instanceof Number){
            return ((Number)userId).longValue();
        }else{
            throw new IllegalArgumentException("JWT토큰에서 userId를 찾을 수 없습니다.");
        }

    }



}

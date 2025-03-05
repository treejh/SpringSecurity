package com.example.springsecurity.jwtexam.basicjwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import javax.crypto.SecretKey;

public class JwtExample {
    public static void main(String[] args) {
        //시크릿키 생성을 위한 알고리즘 방식
        //SecretKey secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);


        //시크릿키를 만들기 위한 걸 직접 지정
        String secret = "abcdefghijklmnopqrstuvwxzy123456";
        byte[] bytes = secret.getBytes(StandardCharsets.UTF_8);
        SecretKey secretKey  = Keys.hmacShaKeyFor(bytes);

        String jwt = Jwts.builder()
                //인증 서버가 여러 개일 수도 있고, 외부 인증 서버 (구글, 카카오 등)를 쓸 수도 있기 때문에,
                //어디서 온 토큰인지 확인하는 용도로 많이 씁니다.
                //누가 발급했는지
                .setIssuer("carami-app")
                //이 토큰의 주인이 누구인지 나타내는 클레임
                //보통은 유저 ID나 유저 고유번호 같은 걸 넣어요.
                .setSubject("carami123")
                //*토큰의 대상 (Audience)**을 나타내는 값입니다.
                //이 토큰이 누구를 위한 것인지를 명확히 지정하는 용도로 사용됩니다.
                //보통 특정 서비스나 시스템 이름, 클라이언트 ID 등을 적어둡니다.
                .setAudience("carami-audience")
                //토큰 만료 시간
                .setExpiration(new Date(System.currentTimeMillis()+36000))
                .claim("role","ADMIN")
                .signWith(secretKey)
                .compact();

        System.out.println(jwt);
        //String newJwt = "eyJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJjYXJhbWktYXBwIiwic3ViIjoiY2FyYW1pMTIzIiwiZXhwIjoxNzQxMTUzNzgzLCJyb2xlIjoiQURNSU4ifQ.12oSjT6xAkP-3ghdz2T-6Ql8Gq3gRtDB5svqFuXD_IM";

        //jwt 파싱, 검증
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                //parseClaimsJwt()	서명 없는 JWT (Plaintext JWT) 처리
                //parseClaimsJws()	서명된 JWT (Signed JWT) 처리
                .parseClaimsJws(jwt)
                .getBody();

        System.out.println(claims.getExpiration());
        System.out.println(claims.getIssuer());
        System.out.println(claims.getSubject());
    }
}

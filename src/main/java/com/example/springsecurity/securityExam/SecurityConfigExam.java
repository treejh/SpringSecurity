package com.example.springsecurity.securityExam;


import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@Slf4j
public class SecurityConfigExam {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http )throws Exception{

        //anyRequest : 모든 요청에 대해서 인증을 요구하겠다.
//        http.authorizeHttpRequests(auth -> auth.anyRequest().authenticated())
//                .formLogin(Customizer.withDefaults());


        //hello, loginForm은 권한 없이 접근이 가능하다.
        http.authorizeHttpRequests(auth ->
                        auth.requestMatchers("/hello","/loginForm")
                                .permitAll()
                                .anyRequest()
                                .authenticated())
                .formLogin(Customizer.withDefaults());

        //return하려는 http .build붙이는거나, 위에 build를 빼고 http.build()나 똑같다 -> stream의 collect 같은거임 ㅇㅇ
        return http.build();
    }
}

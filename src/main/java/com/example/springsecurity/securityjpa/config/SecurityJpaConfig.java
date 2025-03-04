package com.example.springsecurity.securityjpa.config;


import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@Slf4j
public class SecurityJpaConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http)throws Exception{
        http
                .csrf(csfr ->csfr.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/jpa/signupForm","/jpa/registUser","/jpa/loginForm").permitAll()
                        .requestMatchers("/welcome").hasAnyRole("USER","ADMIN")
                        .anyRequest().authenticated()
                );
                //.formLogin(Customizer.withDefaults()


        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }


}

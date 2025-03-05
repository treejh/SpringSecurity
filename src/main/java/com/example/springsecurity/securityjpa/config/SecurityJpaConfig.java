package com.example.springsecurity.securityjpa.config;


import com.example.springsecurity.securityjpa.sercurity.CustomerUserDetailService;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
@Slf4j
public class SecurityJpaConfig {
    private final CustomerUserDetailService customerUserDetailService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http)throws Exception{
        http
                //접근 가능한 url
                .csrf(csfr ->csfr.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/jpa/myinfo","/jpa/signupForm"
                                ,"/jpa/registUser","/jpa/loginForm","/jpa/registUser_role","/jpa/").permitAll()
                        .requestMatchers("/jpa/welcome").hasAnyRole("USER")
                        .anyRequest().authenticated()
                );
                //.formLogin(Customizer.withDefaults()



        //폼
        http.formLogin(form -> form
                .loginPage("/jpa/loginForm") //여기에 html넣는 경우는 없다. controller를 거쳐서 가는것이기 때문
                        .loginProcessingUrl("/login")
                //username이 아닌 email로 파라미터로 받고 싶을때 바꾸면 된다. 하지만 시큐리티는 email->username으로 인식하며 사용된다.
                        //.usernameParameter("/email")
                .defaultSuccessUrl("/jpa/welcome")
        )
                .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/jpa/")
        )
                .userDetailsService(customerUserDetailService);

        //userDetailService



        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }




}

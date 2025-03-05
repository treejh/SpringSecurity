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
                //여러군데에서 로그인 가능하게 하는 허용 횟수 ( 이거 설정 전에는 어디서든지 횟수 상관없이 로그인 가능하게 한다.)
                .sessionManagement(session -> session
                        .maximumSessions(1) // 동시 접속 허용 개수 -> 동시 접속 허용이 1이라면 하나만 접속 가능
                        //디폴트 false, -> 먼저 로그인한 사용자가 차단 된다. -> 차단되어서 페이지가 이동
                        // true -> 나중에 로그인한 사용자가 차단된다. -> 이미 로그인한 애가 있어서 로그인 자체가 안됨
                        .maxSessionsPreventsLogin(true)

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

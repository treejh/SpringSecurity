package com.example.springsecurity.securityexam;


import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@Slf4j
public class SecurityConfigExam {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http )throws Exception{

        //1. anyRequest : 모든 요청에 대해서 인증을 요구하겠다.
//        http.authorizeHttpRequests(auth -> auth.anyRequest().authenticated())
//                .formLogin(Customizer.withDefaults());


        //2. hello, loginForm은 권한 없이 접근이 가능하다. 원하는 Url은 권한 없이 접근이 가능하도록 설정 가능
//        http.authorizeHttpRequests(auth ->
//                        auth.requestMatchers("/hello","/loginForm")
//                                .permitAll()
//                                .anyRequest()
//                                .authenticated())
//                .formLogin(Customizer.withDefaults());


        //3. 폼 로그인 인증 방식을 사용자가 원하는 설정으로
                http.authorizeHttpRequests(auth ->
                        auth.requestMatchers("/hello","/loginForm")
                                .permitAll()
                                .anyRequest()
                                .authenticated())
                .formLogin(formLogin ->formLogin
                        //.loginPage("/loginForm") //원하는 로그인 페이지 설정 (/loginForm -> security가 제공해주는 폼임 ㅇㅇ, 근데 원하는대로 바꾸기 가능함)
                        .defaultSuccessUrl("/success") //로그인에 성공하면 이 페이지로,
                        .failureUrl("/fail")//로그인에 실패하면 이 페이지로
                        .usernameParameter("userId")
                        .passwordParameter("password")
                        // 접근하는 url(/login말고, /jihyun으로 로그인을 바꿀 수 있음)
                        // / long/success, login/success -> jihyun/success, jihyun/fail
                        //loginProcessingUrl-> login_proc는 post방식이 요청으로 들어올때 로그인 로직을 실행시켜준다.
                        .loginProcessingUrl("/login_proc")
                        //성공하면 실행시켜줘
                        .successHandler((request, response, authentication) -> {
                            log.info("로그인 성공 :: "+authentication.getName());
                            response.sendRedirect("/info");
                        })
                        .failureHandler((request, response, exception) -> {
                            log.info("로그인 실패 ::" + exception.getMessage());
                            response.sendRedirect("/login"); //로그인 페이지를 get 방식으로 요청
                            //fail일때 왜 login_proc가지 않는 이유 ?
                            //Login_proc는 post방식의 로그인 로직이기 때문에
                            //실패할때는 화면으로 돌아갈 수 있도록 /login을 사용한다. /login은 시큐리티에서 html을 불러와준다.
                            //로그인을 누를때 login_proc가 실행되는거임 ㅇㅇ
                        })
                        );

        http
                .logout(logout ->logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/hello")//로그아웃에 성공하면 로그인 페이지로 이동
                        //위의 설정으로 로그아웃이 가능하지만, 추가로 하고 싶은 일이 있다면
                        .addLogoutHandler((request, response, authentication) -> {
                            //추가로 해야할일이 뭐 있을까요 ?
                            log.info("로그 아웃 :: 세션, 쿠키 삭제 ");
                            HttpSession session = request.getSession();
                            if(session!=null){
                                session.invalidate(); //세션 삭제
                            }
                        })
                        .deleteCookies()


                );

        //return하려는 http .build붙이는거나, 위에 build를 빼고 http.build()나 똑같다 -> stream의 collect 같은거임ㅇㅇ
        return http.build();
    }
}

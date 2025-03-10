package com.example.springsecurity.oauthexam.config;


import com.example.springsecurity.oauthexam.entity.SocialUser;
import com.example.springsecurity.oauthexam.service.SocialUserService;
import jakarta.servlet.FilterChain;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class OAuthSecurityConfig {

    private final SocialUserService socialUserService;


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        http
                .authorizeHttpRequests(auth->auth
                        .requestMatchers("/loginform","/userregform","/").permitAll()
                        .requestMatchers("/oauth2/**", "/login/oauth2/code/github","/registerSocialUser","/saveSocialUser").permitAll()
                        .anyRequest().authenticated()
                )
                .csrf(csrf -> csrf.disable())
                .formLogin(form -> form.disable())
                .cors(cors -> cors.configurationSource(configurationSource()))
                .httpBasic(httpBasic->httpBasic.disable())
                .oauth2Login(oauth2 -> oauth2
                                .loginPage("/loginform")
                                .failureUrl("/loginFailure")
                        //소셜에서 넘어온 정보를 어떻게 처리할지
                        .userInfoEndpoint(userInfo ->userInfo
                                .userService(this.oauth2UserService())
                                )
                        .successHandler()
                );

        return http.build();
    }

    @Bean
    public OAuth2UserService<OAuth2UserRequest, OAuth2User> oauth2UserService(){
        DefaultOAuth2UserService delegate = new DefaultOAuth2UserService();
        return oauth2UserRequest ->{
            //OAuth로그인이 되면 그 정보를 얻어옴, 그 얻어온 정보를 가지고 로직을 짜는 부분이다.
            OAuth2User oAuth2User = delegate.loadUser(oauth2UserRequest);

            //소셜 로그인이 되었을때.. 해당 소설의 유저 정보를 얻어올 수 있으므로, 그 정보를 처리하는 로직을 여기 둘 수 있다.

            //OAuth2 인증 과정에서 발급된 액세스 토큰(Access Token)을 가져오는 코드
            String token = oauth2UserRequest.getAccessToken().getTokenValue();


            String provider = oauth2UserRequest.getClientRegistration().getRegistrationId();
            String socialId = (String) oAuth2User.getAttributes().get("id");
            String username = (String) oAuth2User.getAttributes().get("login");
            String email = (String) oAuth2User.getAttributes().get("email");
            String avatarUrl = (String) oAuth2User.getAttributes().get("avatar_url");

            socialUserService.saveUpdateUser(socialId,provider,username,email,avatarUrl);
            return oAuth2User;
        };
    }



    public CorsConfigurationSource configurationSource(){
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.addAllowedOrigin("*");
        config.addAllowedHeader("*");

        config.setAllowedMethods(List.of("GET","POST","DELETE"));
        source.registerCorsConfiguration("/**",config);
        return source;
    }


    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}

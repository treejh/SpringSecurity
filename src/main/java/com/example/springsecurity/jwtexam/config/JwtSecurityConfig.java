package com.example.springsecurity.jwtexam.config;


import com.example.springsecurity.jwtexam.jwt.filter.JwtAuthenticationFilter;
import com.example.springsecurity.jwtexam.jwt.util.JwtTokenizer;
import java.security.Security;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;


//JWT를 이용해서 인증을 하고 있다. 로그인 후에 어떤 요청이 들어오면 중간에 가로채서 token을 얻는다.
//얻은 token을 security에 넘기기 위해서 UserDetails 형태로 만든다.

//여기서 인증에 필요한 Authentication객체를 만들고 그걸 감쌀 스래드 로컬 객체 SecurityContextHolder를 만들게 되는데
//얘를 만든 이유는 인증 정보를 서버 내에서 자유롭게 쓰기 위함이다
//
//
//5-1. Authentication 객체는 받아온 토큰을 파싱하는 getAuthentication메서드를 통하여 가져오는데
// 그 객체에서 파싱한 토큰에 userid, name, username같은 자료들과 roles(GrantedAuthority타입으로 넣어준다)로 넣어주게 되고
//5-2. 그리고 실제로 필터에 적용시킬 customUserDetails객체를 만들어서 시큐리티에게 유저의 정보를 넘겨주기 위한 부분을 만들어준다
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class JwtSecurityConfig {

    private final JwtTokenizer jwtTokenizer;

    //filterChain에 만들어진 필터를 추가한다.
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/login").permitAll()
                        .anyRequest()
                        .authenticated())
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenizer), UsernamePasswordAuthenticationFilter.class) //시큐리티 필터가 시작되기전에 토근을 확인하는  로그인 필터가 실행되어야 한다. addFilterBefore사용
                .formLogin(form -> form.disable()) //이걸 disable해야 내가 만든 /login 사용할 수 있음
                .sessionManagement(session -> session
                        //세션을 저장하지 않는다 -> 세션을 사용하지 않겠다는 뜻
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .csrf(csrf -> csrf.disable()) //요청마다 검사해서 이거 disable 해줘야함
                //httpBasic -> 이건 header에 저장하는 방식임
                // 근데 우린 bearer을 사용할 것이기 때문에 basic도 disable한다.
                .httpBasic(httpBasic -> httpBasic.disable())
                //cors -> 리액트할때 자바 스크립트 익스프레스 서버에서 데이터를 제공함
                //리액트 서버는 3000번, 익스프레스는 8080 에서 동작해서 사용함
                //익스프레스 서버에서 cors서버, http localhost 3000번 허락한다고 할때 cors 사용함
                //같은 서버에서 데이터를 요청하면 주지만, 다른 서버에서 데이터를 요청하게 되면 안주기 때문에 cors 설정해야함
                //특정 포트번호를 허락해준다. (어디까지 허용할지 정해줄 수 있다)
                .cors(cors -> cors.configurationSource(configurationSource()));

        return http.build();

    }

    //특정 포트번호를 허락해준다. (어디까지 허용할지 정해줄 수 있다)
    public CorsConfigurationSource configurationSource(){
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        //모두 허용한다고 해놨지만, 특정 포트만 접근할 수 있게 정할 수 있음
        config.addAllowedOrigin("*");

        //헤더 정보-> ContetType/json 뭐 이런거 Authrization 등 원하는 헤더만 들어올 수 있게 할 수 있다
        //*는 다 들어올 수 있다는 뜻 ㅇㅇ
        config.addAllowedHeader("*");

        //모든 메서드 허락
        config.addAllowedMethod("*");

        //특정 메서드 허락
        //get, post, delete 메서드만 허용한다 뭐 이런거 설정할 수 있음
        //아래 코드가 있을때 Put은 동작하지 않는다.
        config.setAllowedMethods(List.of("GET","POST","DELETE"));

        //url 설정을 넣어주는 역할, /admin으로 들어온 url은 특정 config를 넣어줄 수 있따
        //여러개의 config를 만들고, 원하는 url에 맞춰서 사용할 수 있음
        // /admin-> put만 가능하게 , /user -> post만 가능하게
        source.registerCorsConfiguration("/**",config);
        return source;
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

}

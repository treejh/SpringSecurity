package com.example.springsecurity.jwtexam.jwt.exception;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;


public class CustomerAuthenticationEntryPoint implements AuthenticationEntryPoint {

    //시큐리티에서 인증되지 않은 사용자가, 인증해야 사용할 수 있는 보호된 리소스에 접근하려고 할때 동작하는 인터페이스
    //예를들어 /info는 인증된 사용자만 접근할 수 있는데, 인증되지 않은 사용자가 /info에 접근하려고 할때 동작하는 인터페이스
    //사용자가 인증되지 않았을 때 어떻게 응답할지를 정의해주기 위해 사용
    //인증되지 않는 사용자가 접근할때 따로 설정을 해주지 않으면, 아무렇게나 뜸 ㅇㅇ (해당되는 오류나 기본으로 )
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {

    }
}

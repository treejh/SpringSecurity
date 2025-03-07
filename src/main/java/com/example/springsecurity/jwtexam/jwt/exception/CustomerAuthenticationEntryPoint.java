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


    //지금 요청이 rest인지, page인지 확인
    //restControllr, Controller마다 응답을 다르게 ㅎ ㅐ야하기 때문
    //✅ 요청이 AJAX 요청인지, REST API 요청인지 확인하는 메서드
    //브라우저에서 온 요청인지, 프론트에서 AJAX로 호출한 건지 구분할 때 사용
    private boolean isRestRequest(HttpServletRequest request) {
        String requestedWithHeader = request.getHeader("X-Requested-With");

        //ajax 만들때 생성되는 객체 XMLHttpRequest
        return "XMLHttpRequest".equals(requestedWithHeader) || request.getRequestURI().startsWith("/api/");
    }
}

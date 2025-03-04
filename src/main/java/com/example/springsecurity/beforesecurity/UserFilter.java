package com.example.springsecurity.beforesecurity;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;


@Slf4j
@Component
@Order(1)
public class UserFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

      try{
          log.info("UserFilter doFilter 실행 전 "+Thread.currentThread().getName());

          //스레드 로컬에 저장하고 싶은 객체가 존재한다면 ?
          User user = extractUserFromRequest(request);
          UserContext.setUser(user);
          chain.doFilter(request,response);


        log.info("UserFilter doFilter 실행 후");
    }finally {
          UserContext.clear();
      }
    }


    //메서드 분리 법칙
    private User extractUserFromRequest(ServletRequest request){
        //복잡한 로직을 통해서 사용자의 정보를 추출한다면
        String name = request.getParameter("name");
        return new User(name);
    }
}

package com.example.springsecurity.beforeSecurity;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;


@Slf4j
@Component
//@WebFilter(urlPatterns = "/test/*")a- > 모든 요청이 들어왔을때 해당 필터를 거칠것이다.
public class CaramiFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
        log.info("CaramiFilter init()");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        log.info("CaramiFilter dofilter() 실행 전 ");
        chain.doFilter(request,response);
        log.info("CaramiFilter doFilter() 실행 후 ");

    }

    @Override
    public void destroy() {
        log.info("CaramiFilter destory()");
        Filter.super.destroy();
    }
}


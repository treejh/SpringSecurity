package com.example.springsecurity.beforeSecurity;


import jakarta.servlet.FilterRegistration;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {

    @Bean
    public FilterRegistrationBean<CaramiFilter> caramiFilter(){
        FilterRegistrationBean<CaramiFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new CaramiFilter());
        registrationBean.addUrlPatterns("/test/*");
        registrationBean.setOrder(2);

        return registrationBean;


    }

}

package com.example.springsecurity.beforeSecurity;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@Slf4j
public class UserController {

    @GetMapping("/hello")
    public String hello() throws InterruptedException {
        log.info("UserController hello() 실행"+Thread.currentThread().getName());

        Thread.sleep(1000);
        return "hello";
    }

    @GetMapping("/test/hi")
    public String hi() throws InterruptedException {
        log.info("UserController hi() 실행");
        Thread.sleep(5000);
        return "hi";
    }
}

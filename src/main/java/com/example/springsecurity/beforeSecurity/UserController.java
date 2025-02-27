package com.example.springsecurity.beforeSecurity;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@Slf4j
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/hello")
    public String hello() throws InterruptedException {
        log.info("UserController hello() 실행 "+Thread.currentThread().getName());

       //Thread.sleep(1000);

        userService.test();

        return "hello" + UserContext.getUser().getName();
    }

    @GetMapping("/test/hi")
    public String hi() throws InterruptedException {
        log.info("UserController hi() 실행");
        //Thread.sleep(5000);
        return "hi";
    }
}

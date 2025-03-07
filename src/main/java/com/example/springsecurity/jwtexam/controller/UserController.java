package com.example.springsecurity.jwtexam.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserController {

    @GetMapping("/mypage")
    public String myPage(){
        return "exam4/home";
    }
}

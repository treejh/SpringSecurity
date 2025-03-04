package com.example.springsecurity.securityjpa.controller;

import com.example.springsecurity.securityjpa.domain.User;
import com.example.springsecurity.securityjpa.repository.UserRepository;
import com.example.springsecurity.securityjpa.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/jpa")
@Slf4j
public class UserController {
    private final UserService userService;

    //회원가입 폼 요청
    @GetMapping("/signupForm")
    public String signUpPage(){
        return "securityjpa/users/signup";
    }


    //로그인 폼 요청
    @GetMapping("/loginForm")
    public String loginPage(){
        return "securityjpa/users/loginform";
    }

    //회원가입 요청
    @PostMapping("/registUser")
    public String registUser(@ModelAttribute User user){
        if(userService.existUser(user.getUsername())){
            log.info("이미 사용중인 아이디 :: "+ user.getUsername());
            return "securityjpa/user-error";
        }
        userService.registUser(user);
        return "redirect:/jpa/loginForm";
    }

    @GetMapping("/myinfo")
    public  String successPage(){
        return "securityjpa/myinfo";
    }

    @PostMapping ("/login")
    public String login(){
        return "securityjpa/myinfo";
    }





}

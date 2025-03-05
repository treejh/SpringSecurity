package com.example.springsecurity.securityjpa.controller;

import com.example.springsecurity.securityjpa.domain.User;
import com.example.springsecurity.securityjpa.domain.UserRegisterDTO;
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
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
@RequestMapping("/jpa")
@Slf4j
public class UserController {
    private final UserService userService;


    @GetMapping("/welcome")
    public String welcome(){
        return "securityjpa/welcome";
    }

    @GetMapping("/myinfo")
    public String myinfo(){
        return "securityjpa/myinfo";
    }

    @GetMapping("/")
    public String home(){
        return "securityjpa/home";
    }

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

    //회원가입 요청(역할 여러개 들어올 경우)
    @PostMapping("/registUser_role")
    public String registUserRole(@ModelAttribute UserRegisterDTO userRegisterDTO){
        if(userService.existUser(userRegisterDTO.getUsername())){
            log.info("이미 사용중인 아이디 :: "+ userRegisterDTO.getUsername());
            return "securityjpa/user-error";
        }

        User user = userService.registerUserRegisterDTO(userRegisterDTO);
        System.out.println(user.getUsername());

        return "redirect:/jpa/loginForm";
    }

}

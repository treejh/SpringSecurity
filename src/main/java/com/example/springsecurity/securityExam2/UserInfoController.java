package com.example.springsecurity.securityExam2;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserInfoController {

    @GetMapping("/userinfo")
    public String userInfo(){
        return "exam3/user-info";
    }
}

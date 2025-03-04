package com.example.springsecurity.securityExam2;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/shop")
public class ShopController {


    @GetMapping
    public String hello(){
        return "hello";
    }


    @GetMapping("/abc")
    public String aaa(){
        return "aaa";
    }

    @GetMapping("/bbb")
    public String bbb(){
        return "bbb";
    }

    @GetMapping("/ccc")
    public String ccc(){
        return "ccc";
    }

}

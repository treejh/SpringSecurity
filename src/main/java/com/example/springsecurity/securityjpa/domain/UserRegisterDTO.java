package com.example.springsecurity.securityjpa.domain;

import java.util.List;

public class UserRegisterDTO {
    private String username;
    private String password;
    private String name;
    private String email;
    //html의 이름이 roles이다.
    private List<String> roles;

}

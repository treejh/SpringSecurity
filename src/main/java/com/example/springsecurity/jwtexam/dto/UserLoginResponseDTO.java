package com.example.springsecurity.jwtexam.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
//응답에는 값이 바뀌지 않아야 하기때문에 Setter말고 builder를 사용할것이다.
public class UserLoginResponseDTO {
    private String accessToken;
    private String refreshToken;
    private Long userId;
    private String name;

}

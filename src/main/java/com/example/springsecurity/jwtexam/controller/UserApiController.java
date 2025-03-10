package com.example.springsecurity.jwtexam.controller;


import com.example.springsecurity.jwtexam.domain.RefreshToken;
import com.example.springsecurity.jwtexam.domain.Role;
import com.example.springsecurity.jwtexam.domain.User;
import com.example.springsecurity.jwtexam.dto.UserLoginResponseDTO;
import com.example.springsecurity.jwtexam.jwt.util.JwtTokenizer;
import com.example.springsecurity.jwtexam.security.dto.UserLoginDto;
import com.example.springsecurity.jwtexam.service.RefreshTokenService;
import com.example.springsecurity.jwtexam.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class UserApiController {

    private final UserService userService;
    private final RefreshTokenService refreshTokenService;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenizer jwtTokenizer;

//    @PostMapping("/refreshToken")
//    public ResponseEntity<?> requestRefresh(){
//        //리프레시 토큰... 무슨 일 하면 좋을까 ?
//
//        //1. 리프레시토큰을 받으면, 내 DB에 있는지, 있을때만 다시 accessToken을 발급한다.
//
//    }

    @GetMapping("/api/info")
    public String info(){
        return "info";
    }


    @PostMapping("/login")
    public ResponseEntity login(@RequestBody UserLoginDto userLoginDto, HttpServletResponse response){

        //1. username이 DB에 있는지 확인
        User user = userService.findByUsername(userLoginDto.getUsername());

        if(user==null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("존재하지 않는 아이디 입니다 "+userLoginDto.getUsername());
        }


        //2. 가지고온 user의 비밀번호가 맞는지 확인
        if(!passwordEncoder.matches(userLoginDto.getPassword(),user.getPassword())){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("비밀번호가 올바르지 않습니다");
        }

        //3. 여기까지 왔다면, 유저도있고, 비밀번호도 맞기 때문에 토큰 생성 또는 확인 한다. 일단 roles 넣어줌
        List<String> roles = user.getRoles().stream().map(Role::getName).collect(Collectors.toList());

        //4. 토큰 발급 - accessToken, refreshToken 두개 발급할거임
        String accessToken = jwtTokenizer.createAccessToken(user.getId(),user.getEmail(),
                user.getName(), user.getUsername()
                ,roles);


        String refreshToken = jwtTokenizer.createRefreshToken(user.getId(),user.getEmail(),
                user.getName(), user.getUsername()
                ,roles);

        //5. 생성한 refreshToken DB에 저장하기
        RefreshToken refreshTokenEntity = new RefreshToken();
        refreshTokenEntity.setValue(refreshToken);
        refreshTokenEntity.setUserid(user.getId());

        refreshTokenService.addRefreshToken(refreshTokenEntity);

        //만약 쿠키로 저장하고 싶다면 ?
        Cookie accessTokenCookie = new Cookie("accessToken",accessToken);
        //보안을 위해 필요한 부분
        //http에서만 사용할 수 있다. 쿠키값을 자바스크립트에서 접근할 수 없다.
        accessTokenCookie.setHttpOnly(true);
        accessTokenCookie.setPath("/");
        //쿠키는 시간이 s초단위이다.
        //jwt는 ms초 단위이다.
        //쿠키는 초단위이기 때문에 ms로 사용을 위해 1000으로 나눈다.
        //toIntExact -> long타입을 int로 안전하게 바꿔준다.
        accessTokenCookie.setMaxAge(Math.toIntExact(JwtTokenizer.ACCESS_TOKEN_EXPIRE_COUNT/1000));

        //만약 쿠키로 저장하고 싶다면 ?
        Cookie refreshTokenCookie = new Cookie("refreshToken",refreshToken);
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setPath("/");
        refreshTokenCookie.setMaxAge(Math.toIntExact(JwtTokenizer.ACCESS_TOKEN_EXPIRE_COUNT/1000));

        //쿠키를 응답에 넣어준다.
        response.addCookie(accessTokenCookie);
        response.addCookie(refreshTokenCookie);

        //6. 응답으로 보낼 값을 준비해준다.
        UserLoginResponseDTO loginResponseDto = UserLoginResponseDTO.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .userId(user.getId())
                .name(user.getName())
                .build();


        return ResponseEntity.ok(loginResponseDto);



    }
}

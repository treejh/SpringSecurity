package com.example.springsecurity.oauthexam.security;

import com.example.springsecurity.oauthexam.entity.SocialLoginInfo;
import com.example.springsecurity.oauthexam.entity.User;
import com.example.springsecurity.oauthexam.service.SocialLoginInfoService;
import com.example.springsecurity.oauthexam.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import com.example.springsecurity.oauthexam.entity.Role;


@Component
@RequiredArgsConstructor
//> OAuth인증이 끝난 후에는 어떻게 ?
//> 시큐리티 컨텍스트에 인증 정보를 넣는 일은 필수,  나머지 부분들은 어떻게할지 정해서 수행하도록 코드를 작성하면 됩니다.
public class CustomOAuth2AuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final UserService userService;
    private final SocialLoginInfoService socialLoginInfoService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        //요청 정보로부터 provider를 얻어온다.
        //registrationId는 provider(네이버, git) 등 마다 다르다.
        //redirect url : "{baseUrl}/login/oauth2/code{registraionId}

        String requestURI = request.getRequestURI();

        //provider를 통해서 registraionId (ex. github, naver) 를 가지고 온다.
        String provider = extractProviderFromUri(requestURI);

        //provider가 없는 경로가 요청되었다고 하는 것은 문제가 발생한 것
        if(provider == null){
            response.sendRedirect("/");
            return; //return 해줘야 다시 돌아감
        }
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        DefaultOAuth2User defaultOAuth2User = (DefaultOAuth2User) auth.getPrincipal();


        String socialId = defaultOAuth2User.getAttribute("id").toString();
        String name = defaultOAuth2User.getAttribute("name").toString();

        Optional<User> userOptional = userService.findByProviderAndSocialId(provider, socialId);

        //이 사용자가 우리 서비스에 정복 있는지( 이미 사용한 적 있으면 User 정보를 가지고 있을 거싱다)
        if(userOptional.isPresent()){

            User user = userOptional.get();

            //이때 얻어낸 User 정보를 어디에 넣어줄까요 ?
            CustomerUserDetails customerUserDetails = new CustomerUserDetails(user.getName(),
                    user.getPassword(),user.getName()
                    //Roles 는 set이여서, List String으로 바꿔줘야 한다.
                    ,user.getRoles().stream()
                    .map(Role::getName)
                    .collect(Collectors.toList()));

            //토큰을 만들어주는게 아니라, Authentication 생성하기 위해서 사용하는거임 ㅇㅇ
            //토큰 아님 xxx Authentication객체이다 !! OAuth는 토큰 필요 없다.
//            Authentication newAuth =
//                    new UsernamePasswordAuthenticationToken(customerUserDetails,null);
//
//            SecurityContextHolder.getContext().setAuthentication(newAuth);

            Authentication newAuth =
                    new UsernamePasswordAuthenticationToken(customerUserDetails,null,customerUserDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(newAuth);


            //일이 다 끝나면 redirect할 수 있도록 한다.
            response.sendRedirect("welcome");


        }
        else{
            //소셜로 아직 회원가입이 안되어 있을때 .. 무슨일을 해야 할까요 ?
            //이 사용자가 우리 애플리케이션에 처음으로 들어왔을때
            //사용자의 정보를 우리 서비스에더 남겨두고 싶다면 -> 소셜로그인 수행 후 저장할 수 있는 애들 ㅇㅇ
            //지금 여기 상황은 소셜로그인 후에 상황의 로직을 짜공 ㅣㅆ는거임
            //소셜 로그인을 했다면, 서버에도 관련 정보를 저장해야 할거임
            //소셜 로그인 후 회원가입 진행


            SocialLoginInfo socialLoginInfo = socialLoginInfoService.saveSocialLoginInfo(provider,socialId);

            response.sendRedirect("/registerSocialUser?provider="+provider
                    +"&socialId="+socialId+"&name="+name+"&uuid="+socialLoginInfo.getUuid());

        }

    }

    //uri에서 값을 얻으면 redirect url : "{baseUrl}/login/oauth2/code{registraionId} 이렇게 값을 얻어온다.
    //먄약 위의 uri가 들어오면 실제로 이 uri 값은
    //localhost:8080/lgoin/oauth2/code/github 이렇게 데이터를 들어올거임 ㅇㅇ
    //uri로 들어오면 /login/oauth2/code{registraionId} 이렇게 들어옴 , baseURL 없어지고
    private String extractProviderFromUri(String uri) {

        //uri가 Null인거면 주소 안들어온거임
        if(uri == null || uri.isBlank()) {
            return null;
        }


        //아래와 같은 url이 아니면 null
        if(!uri.startsWith("/login/oauth2/code/")){
            return null;
        }



        // 예: /login/oauth2/code/github -> github
        //맨 마지막 provider만 필요하기 때문에 잘라준다.
        String[] segments = uri.split("/");
        return segments[segments.length - 1];
    }
}

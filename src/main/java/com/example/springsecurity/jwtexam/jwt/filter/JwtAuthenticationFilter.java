package com.example.springsecurity.jwtexam.jwt.filter;

import com.example.springsecurity.jwtexam.jwt.token.JwtAuthenticationToken;
import com.example.springsecurity.jwtexam.jwt.util.JwtTokenizer;
import com.example.springsecurity.jwtexam.security.dto.CustomUserDetails;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;


@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenizer jwtTokenizer;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        //request로부터 토큰을 얻어와야 한다.-> header로 들어오거나 cookie 로 들어오거나 할 수 있음
        //-> 이떄문에 토큰을 얻는 메서드가 하나 있는게 좋을듯 (코드 길어질 것 같다 and method 분리)

        String token = getToken(request);
        //토큰 값이 있다면 -> 토큰으로부터 값을 추출해서 security가 원하는 authentication 객체를 만들어야함
        if(StringUtils.hasText(token)){
            try{
                //security에게 authentication를 넘기기 위해서 원하는 데이터를 얻어와서 authentication를 만들어준다.
                Authentication authentication = getAuthentication(token);

                //만들어진 authentication를 SecurityContextHolder의 SecurityContext 로 넘긴다.
                SecurityContextHolder.getContext().setAuthentication(authentication);

            }catch (Exception e){
                log.error("JWT Filter - Internal Filter : {} ",token,e);
                SecurityContextHolder.clearContext();
                throw new BadCredentialsException("JWT  Filter - Internal Filter");
            }
        }


        filterChain.doFilter(request,response);

    }

    private Authentication getAuthentication(String token){
        Claims claims = jwtTokenizer.parseAccessToken(token);
        String email = claims.getSubject();
        Long userId = claims.get("userId", Long.class);
        String name = claims.get("name", String.class);
        String username = claims.get("username", String.class);

        //권한
        List<GrantedAuthority> grantedAuthorities = getGrantedAuthority(claims);
        //userDetails
        CustomUserDetails customUserDetails = new CustomUserDetails(username,"",name,grantedAuthorities);


        //권한 , 정보들, password -> But jwt에서는 password를 직접 쓰지 않기 때문에 Null
        //securittContext안에는 Authenticaiton 객체가 필요한데, Authenticaiton객체를 만들기 위해 JwtAuthenticationToken를 사용했다
        //아래처럼 하면 원하는 데이터를 넘길 수 있음 ㅇㅇ
        return new JwtAuthenticationToken(grantedAuthorities,customUserDetails,null);

    }

    //roles를 받아오는 부분은 좀 큼
    private List<GrantedAuthority> getGrantedAuthority(Claims claims){
        List<String>roles = (List<String>)claims.get("roles");
        return roles.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
    }


    public String getToken(HttpServletRequest request){


        //헤더에 있는지 확인
        String authorization = request.getHeader("Authorization");
        if(StringUtils.hasText(authorization) && authorization.startsWith("Bearer ")){
            return authorization.substring(7);
        }

        //쿠키에 있는지 확인
        Cookie[] cookies = request.getCookies();
        if(cookies!=null){
            for(Cookie cookie : cookies){
                if("accessToken".equals(cookie.getName())){
                    return cookie.getValue();
                }
            }
        }

        //헤더에도 없고, 쿠키에도 없다면 ?
        return null;

    }
}

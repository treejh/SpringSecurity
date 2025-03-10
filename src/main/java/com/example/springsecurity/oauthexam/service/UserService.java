package com.example.springsecurity.oauthexam.service;


import com.example.springsecurity.oauthexam.dto.userDto.SocialUserRequestDto;
import com.example.springsecurity.oauthexam.entity.User;
import com.example.springsecurity.oauthexam.repositroy.UserRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    //private final PasswordEncoder passwordEncoder;

    //oauth를 사용하고, 추가 정보를 받고 싶을때 사용한다. 추가정보를 저장
    //oauth를 저장하고, 추가적인 아이디 비밀번호도 사용
    //소셜로그인으로  인증을 성공하고 나면...   어떤일을 할래???  --  우리가 구현 할텐데..
    //이때...   어떻게 할지..  요 부분을 해보려고 하는거예요.
    @Transactional
    public User saveUser(SocialUserRequestDto requestDto,PasswordEncoder passwordEncoder){
        User user = new User();
        user.setUsername(requestDto.getUsername());
        user.setName(requestDto.getName());
        user.setEmail(requestDto.getEmail());
        user.setSocialId(requestDto.getSocialId());
        user.setProvider(requestDto.getProvider());
        user.setPassword(passwordEncoder.encode(""));// 소셜 로그인을 진행하는 사용자는 비밀번호를 비워둔다.

        return userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public User findByUsername(String username){
        return userRepository.findByUsername(username);
    }

    @Transactional(readOnly = true)
    public Optional<User> findByProviderAndSocialId(String provider, String socialId){
        return userRepository.findByProviderAndSocialId(provider,socialId);
    }
}

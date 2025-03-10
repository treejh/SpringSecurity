package com.example.springsecurity.oauthexam.service;


import com.example.springsecurity.oauthexam.dto.userDto.SocialUserRequestDto;
import com.example.springsecurity.oauthexam.entity.SocialUser;
import com.example.springsecurity.oauthexam.repositroy.SocialUserRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SocialUserService {
    private final SocialUserRepository socialUserRepository;

    //소셜에서 보내준 정보를 저장하기 위한 메서드
    //정보가 바뀔 수 있어서 Update로 함 ㅇㅇ
    @Transactional
    public SocialUser saveUpdateUser(String socialId, String provider, String username, String email,
                                     String avatarUrl){

        //이미 로그인한 사용자인지 확인하기 위해서, exist User 확인
        //로그인 했으면 데이터 존재, 안했으면 데이터 존재 안함
        Optional<SocialUser> existUser = socialUserRepository.findBySocialIdAndProvider(socialId, provider);

        SocialUser socialUser;

        //이미 소셜 정보를 가진 사용자라면 한번 로그인한 사용자라는 뜻
        if(existUser.isPresent()){
            socialUser = existUser.get();
            socialUser.setUsername(username);
            socialUser.setEmail(email);
            socialUser.setAvatarUrl(avatarUrl);
        }
        //처음 방문했다면 로그인을 하지 않았기 때문에, DB에 저장이 안되어져 있을 것이다.
        else{
            socialUser = new SocialUser();

            //처음방문
            socialUser = new SocialUser();
            socialUser.setSocialId(socialId);
            socialUser.setUsername(username);
            socialUser.setEmail(email);
            socialUser.setAvatarUrl(avatarUrl);
            socialUser.setProvider(provider);
        }

        return socialUserRepository.save(socialUser);
    }
}

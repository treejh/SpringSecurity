package com.example.springsecurity.oauthexam.controller;


import com.example.springsecurity.oauthexam.dto.userDto.SocialUserRequestDto;
import com.example.springsecurity.oauthexam.entity.SocialLoginInfo;
import com.example.springsecurity.oauthexam.security.CustomerUserDetails;
import com.example.springsecurity.oauthexam.service.SocialLoginInfoService;
import com.example.springsecurity.oauthexam.service.UserService;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final SocialLoginInfoService socialLoginInfoService;
    private final PasswordEncoder passwordEncoder;


    @GetMapping("/")
    public String home(){
        return "oauth/home";}

    @GetMapping("/welcome")
    public String welcome(){
        return "oauth/welcome";
    }

    @GetMapping("/loginform")
    public String loginform(){
        return "oauth/users/loginform";
    }
    @GetMapping("/registerSocialUser")
    public String registerSocialUser(@RequestParam("provider") String provider,
                                     @RequestParam("name") String name,
                                     @RequestParam("socialId") String socialId,
                                     @RequestParam("uuid") String uuid, Model model){
        //provider, socialId, name, uuid

        model.addAttribute("provider",provider);
        model.addAttribute("socialId",socialId);
        model.addAttribute("name",name);
        model.addAttribute("uuid",uuid);

        return "oauth/users/registerSocialUser";
    }


    @PostMapping("/saveSocialUser")
    public String saveSocialUser(@ModelAttribute SocialUserRequestDto requestDto){
        Optional<SocialLoginInfo> socialLoginInfoOptioanl = socialLoginInfoService.findByProviderAndUuidAndSocialId(
                requestDto.getProvider(),
                requestDto.getUuid(), requestDto.getSocialId());

        if(socialLoginInfoOptioanl.isPresent()){
            SocialLoginInfo socialLoginInfo = socialLoginInfoOptioanl.get();
            LocalDateTime now = LocalDateTime.now();

            Duration duration = Duration.between(socialLoginInfo.getCreateAt(), now);
            //duration이 20분 보다 초과 됐다면?
            if(duration.toMinutes()> 20){
                return "redirect:/error"; //20분 이상 경과하면 에러페이지로 리다이렉트
            }

            userService.saveUser(requestDto,passwordEncoder);
            return "redirect:/info";
        }else{
            return "redirect:/error";
        }
    }


    @GetMapping("/info")
    public String info(@AuthenticationPrincipal CustomerUserDetails customerUserDetails, Model model){
        model.addAttribute("user",customerUserDetails);

        return"oauth/info";

    }

}

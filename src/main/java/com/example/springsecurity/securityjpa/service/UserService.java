package com.example.springsecurity.securityjpa.service;


import com.example.springsecurity.securityjpa.domain.Role;
import com.example.springsecurity.securityjpa.domain.User;
import com.example.springsecurity.securityjpa.domain.UserRegisterDTO;
import com.example.springsecurity.securityjpa.repository.RoleRepository;
import com.example.springsecurity.securityjpa.repository.UserRepository;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public User registUser(User user){
        //롤(권한) 정보를 User 엔티티에 채워줄 필요가 있다.


        //1. 회원가입 요청이 들어오면 USER 권한으로 가입
        Role userRole = roleRepository.findByName("USER").get();
        //user.setRoles(userRole);
        user.setRoles(Collections.singleton(userRole));

        //2. 패스워드는 반드시 암호화해서 넣어준다 ( 인코딩)
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        //3. userRepository에 넣어준다.
        return userRepository.save(user);


    }

    @Transactional
    public User registerUserRegisterDTO(UserRegisterDTO userRegisterDTO){
        String encodePassword = passwordEncoder.encode(userRegisterDTO.getPassword());
        //롤 객체를 하나씩 얻어온다.
        Set<Role> roles =  userRegisterDTO.getRoles().stream()
                .map(roleRepository::findByName)
                .flatMap(Optional::stream) //Optioanl이 비어있다면, 그냥 무시하고 지나간다. (값만 추출 하기 위해서)
                .collect(Collectors.toSet());
        User user = new User();
        user.setUsername(userRegisterDTO.getUsername());
        user.setPassword(encodePassword); //인코딩한 패스워드를 넣어주야 한다.
        user.setName(userRegisterDTO.getName());
        user.setEmail(userRegisterDTO.getEmail());
        user.setRoles(roles); //Roles

        return userRepository.save(user);


    }

    //username에 해당하는 사용자가 있는지 체크
    public boolean existUser(String username){
        return userRepository.existsByUsername(username);

    }




}

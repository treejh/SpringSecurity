package com.example.springsecurity.securityjpa.sercurity;

import com.example.springsecurity.securityjpa.domain.Role;
import com.example.springsecurity.securityjpa.domain.User;
import com.example.springsecurity.securityjpa.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.User.UserBuilder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;


@Service
@RequiredArgsConstructor
@Slf4j
public class CustomerUserDetailService implements UserDetailsService {

    private final UserRepository userRepository;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if(user==null){
            throw new UsernameNotFoundException(username + " 에 해당하는 사용자가 없습니다. ");
        }

        log.info("username :: "+ username);

        UserBuilder userBuilder = org.springframework.security.core.userdetails.User.withUsername(username);
        userBuilder.password(user.getPassword());
        userBuilder.roles(user.getRoles().stream().map(Role::getName).toList().toArray(new String[0]));


        return userBuilder.build();
    }
}

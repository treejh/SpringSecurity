package com.example.springsecurity.securityjpa;

import com.example.springsecurity.beforesecurity.Application;
import com.example.springsecurity.securityjpa.domain.Role;
import com.example.springsecurity.securityjpa.repository.RoleRepository;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@Slf4j
public class SecurityJpaApplication {
    public static void main(String[] args) {
        SpringApplication.run(SecurityJpaApplication.class, args);
    }

    @Bean
    public CommandLineRunner run(RoleRepository roleRepository){
        return args -> {
            if(roleRepository.count()==0){
                Role role = new Role();
                role.setName("USER");

                Role adminRole = new Role();
                adminRole.setName("ADMIN");

                roleRepository.saveAll(List.of(role,adminRole));
                log.info("USER,ADMIN 권한이 추가되었습니다 ");
            }else{
                log.info("권한 정보가 이미 존재합니다. ");
            }
        };
    }
}

package com.example.springsecurity.securityjpa.repository;

import com.example.springsecurity.securityjpa.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Long> {

    User findByUsername(String username);
    boolean existsByUsername(String username);
    User findByEmail(String email);
}

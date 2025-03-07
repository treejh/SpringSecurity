package com.example.springsecurity.jwtexam.repository;

import com.example.springsecurity.jwtexam.domain.Role;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(String name);


}

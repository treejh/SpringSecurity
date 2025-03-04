package com.example.springsecurity.securityjpa.repository;

import com.example.springsecurity.securityjpa.domain.Role;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(String name);


}

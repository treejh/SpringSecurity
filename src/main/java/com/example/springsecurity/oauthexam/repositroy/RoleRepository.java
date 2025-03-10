package com.example.springsecurity.oauthexam.repositroy;

import com.example.springsecurity.oauthexam.entity.Role;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(String name);
}

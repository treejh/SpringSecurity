package com.example.springsecurity.oauthexam.entity;

import com.example.springsecurity.jwtexam.domain.Role;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.Set;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "lion_users")
@Getter
@Setter
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String username;

    @Column(nullable = false, length = 100)
    private String password;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(nullable = false, length = 100)
    private String email;

    @Column(name="registration_date",nullable = false,updatable = false)
    private LocalDateTime registrationDate = LocalDateTime.now();

    //oauth 필드 추가

    //컬럼 추가.
    @Column(name = "social_id")
    private String socialId;

    //깃허브 ,네이버 등 어디서 인증을 제공하는지
    private String provider;

    //manyToMany는 본인의 테이블에 id를 가질 수 없기 때문에, 연결 테이블을 만들어준다.
    @ManyToMany(fetch= FetchType.EAGER)
    @JoinTable(
            name="user_roles",
            joinColumns = @JoinColumn(name="user_id"),
            inverseJoinColumns = @JoinColumn(name="role_id")
    )
    private Set<Role> roles;


}

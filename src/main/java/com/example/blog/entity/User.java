package com.example.blog.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "users")
public class User implements UserDetails { // UserDetails 를 상속받아서 인증객체로 사용할 것임.

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "nickname", unique = true)
    private String nickname;

    @Builder
    public User(String email, String password, String nickname) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
    }

    public User update(String nickname){
        this.nickname = nickname;
        return this;
    }

    @Override // 사용자가 가지고 있는 권한의 목록을 반환 => 현재는 사용자 이외의 권한이 없기 때문에 user 권한만 담음
    public Collection<? extends GrantedAuthority> getAuthorities(){
        return List.of(new SimpleGrantedAuthority("user"));
    }

    @Override // 사용자의 id를 반환 => 고유한 값 (unique = true)
    public String getUsername(){
        return email;
    }

    @Override
    public String getPassword(){
        return password;
    }

    @Override // 계정 만료 여부 반환
    public boolean isAccountNonExpired(){
        return true; // true -> 만료되지 않음
    }

    @Override // 계정 잠금 여부 반환
    public boolean isAccountNonLocked(){
        return true; // true -> 잠금되지 않음
    }

    @Override // 패스워드의 만료 여부 반환
    public boolean isCredentialsNonExpired(){
        return true; // true -> 만료되지 않음
    }

    @Override // 계정 사용 가능 여부 반환
    public boolean isEnabled(){
        return true; // true -> 사용 가능
    }
}

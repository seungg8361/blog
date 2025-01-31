package com.example.blog.repository;

import com.example.blog.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    // Optional => null 로 인해 발생할 수 있는 NullPointerException 을 방지
    Optional<User> findByEmail(String email);
}

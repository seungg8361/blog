package com.example.blog.service;

import com.example.blog.dto.AddUserRequest;
import com.example.blog.entity.User;
import com.example.blog.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    // 글 저장할 때 builder 를 사용했던 것 처럼 user 정보를 저장하기 위해 builder 패턴을 사용함.
    public Long save(AddUserRequest request){
        return userRepository.save(User.builder()
                .email(request.getEmail())
                .password(bCryptPasswordEncoder.encode(request.getPassword()))
                .build()).getId();
    }
    // 리프레시 토큰을 전달받아 새로운 엑세스 토큰을 만들기 위해 유저 ID로 유저를 검색
    public User findById(Long userId){
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Unexpected user"));
    }
}

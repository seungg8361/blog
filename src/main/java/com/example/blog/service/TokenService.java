package com.example.blog.service;

import com.example.blog.config.jwt.TokenProvider;
import com.example.blog.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;

@RequiredArgsConstructor
@Service
public class TokenService {

    private final TokenProvider tokenProvider;
    private final RefreshTokenService refreshTokenService;
    private final UserService userService;

    // 전달받은 refreshToken 으로 새로운 엑세스 토큰 생성
    public String createNewAccessToken(String refreshToken) {
        if(!tokenProvider.validToken(refreshToken)) {
            throw new IllegalArgumentException("Unexpected token");
        }
        // 해당 리프레시 토큰이 어떤 사용자(userId) 와 연결되어 있는지 확인
        Long userId = refreshTokenService.findByRefreshToken(refreshToken).getUserId();
        // 해당 유저가 있는지 확인
        User user = userService.findById(userId);

        return tokenProvider.generateToken(user, Duration.ofHours(2));
    }
}

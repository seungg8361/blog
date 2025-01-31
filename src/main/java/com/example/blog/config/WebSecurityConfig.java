package com.example.blog.config;

import com.example.blog.service.UserDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import static org.springframework.boot.autoconfigure.security.servlet.PathRequest.toH2Console;

@Configuration  // 설정 클래스임을 나타낸다.
@EnableWebSecurity // Spring Security 를 활성화하기 위함.
@RequiredArgsConstructor
public class WebSecurityConfig {

    private final UserDetailService userService;

    @Bean
    public WebSecurityCustomizer configure(){
        return (web) -> web.ignoring() // 비활성화
                .requestMatchers(toH2Console())
                .requestMatchers(new AntPathRequestMatcher("/static/**")); // Ant 스타일로 URL 설정
        // ex) static 하위 경로의 모든 파일에 적용
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeRequests(auth -> auth
                        .requestMatchers(
                                new AntPathRequestMatcher("/login"),
                                new AntPathRequestMatcher("/signup"),
                                new AntPathRequestMatcher("/user")
                        ).permitAll().anyRequest().authenticated())

                .formLogin(formLogin -> formLogin    // 기본 로그인 폼 설정
                        .loginPage("/login")
                        .defaultSuccessUrl("/articles"))

                .logout(logout -> logout               // 로그아웃 페이지 설정
                        .logoutSuccessUrl("/login")
                        .invalidateHttpSession(true))

                .csrf(AbstractHttpConfigurer::disable) // csrf 비활성화
                .build();
    }

    // 인증 관리자 관련 설정
    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http, BCryptPasswordEncoder bCryptPasswordEncoder,
                                                       UserDetailService userDetailService) throws Exception {
        // Spring Security 에서 제공하는 인증 제공자로 DB 에서 사용자 정보를 조회하고 인증을 수행하는 역할
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userService); // 사용자 정보 서비스 설정
        authProvider.setPasswordEncoder(bCryptPasswordEncoder);
        return new ProviderManager(authProvider);
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

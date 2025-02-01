package com.example.blog.config;

import com.example.blog.config.jwt.TokenProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
/* 클라이언트에서 요청이 오면 헤더값을 비교해서 토큰이 있는지 확인하고 유효 토큰이라면, SecurityContextHolder 에 정보 저장
   SecurityContext : 인증 객체가 저장되는 곳. 스레드마다 공간을 할당하는 스레드 로컬에 저장되므로 아무 곳에서나 참조가능.
   이러한 SecurityContext 객체를 저장하는 객체가 SecurityContextHolder 이다.

   OncePerRequestFilter =>
   1. 클라이언트가 API 요청을 보냄
   2. Spring Security 의 필터 체인에 JwtAuthenticationFilter 가 포함됨
   3. 필터가 실행되면서 한 번만 JWT 토큰을 검증
   4️. SecurityContextHolder 에 인증 정보 저장
   5. 이후 동일한 요청에서 이 필터가 다시 실행되지 않음
*/
// 요청당 한 번만 실행되는 필터를 만들기 위해 사용
public class TokenAuthenticationFilter extends OncePerRequestFilter {

    private final TokenProvider tokenProvider;
    private final static String HEADER_AUTHORIZATION = "Authorization"; // 헤더의 name
    private final static String HEADER_PREFIX = "Bearer "; // 토큰 앞에 붙는 접두사

    @Override
    protected void doFilterInternal(
            HttpServletRequest request, HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException{

        // 요청 헤더의 Authorization 키의 값 조회
        String authorizationHeader = request.getHeader(HEADER_AUTHORIZATION);
        // 가져온 값에서 접두사 제거
        String token = getAccessToken(authorizationHeader);
        // 가져온 토큰이 유효한지 확인, 유효하면 인증 정보 설정
        if(tokenProvider.validToken(token)){
            // 인증 정보를 가져온다.
            Authentication authentication = tokenProvider.getAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        // 다음 필터로
        filterChain.doFilter(request, response);
    }

    private String getAccessToken(String authorizationHeader){
        // 넘어온 헤더의 이름이 비어있지 않고, Bearer 로 시작한다면 Bearer 을 인덱싱해서 순수한 토큰만 반환한다.
        if(authorizationHeader != null && authorizationHeader.startsWith(HEADER_PREFIX)){
            return authorizationHeader.substring(HEADER_PREFIX.length());
        }
        return null;
    }
}

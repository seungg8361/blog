package com.example.blog.config.jwt;

import com.example.blog.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Collections;
import java.util.Date;
import java.util.Set;

import static io.jsonwebtoken.Header.JWT_TYPE;

@RequiredArgsConstructor
@Service
// 토큰을 생성, 유효성 검사, 토큰에서 필요한 정보를 가져오는 클래스
public class TokenProvider {

    private final JwtProperties jwtProperties;

    public String generateToken(User user, Duration expiredAt) {
        Date now = new Date();
        return makeToken(new Date(now.getTime() + expiredAt.toMillis()), user);
    }

    public String makeToken(Date expiry, User user){
        Date now = new Date();

        return Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE) // 헤더 typ : JWT
                // 내용 iss => properties 파일에서 설정한 값
                .setIssuer(jwtProperties.getIssuer())
                .setIssuedAt(now) // 내용 iat : 생성시간
                .setExpiration(expiry) // 내용 exp : 만료시간
                .setSubject(user.getEmail()) // 내용 sub : 유저의 이메일
                // 클레임 id : 유저의 ID
                .claim("id", user.getId())
                // 서명 : 비밀값과 함께 해시값을 HS256 방식으로 암호화
                .signWith(SignatureAlgorithm.HS256, jwtProperties.getSecretKey())
                .compact();
    }
    // 토큰을 검증하는 메서드
    public boolean validateToken(String token){
        try{
            Jwts.parser()
                    .setSigningKey(jwtProperties.getSecretKey())
                    .parseClaimsJws(token); // token 을 해석해서 내부의 페이로드 부분을 추출 => 헤더와 서명을 제외한 내용
            return true;
        }catch (Exception e){
            return false;
        }
    }
    // 토큰 기반으로 인증 정보를 가져오는 메서드 => Authentication : 인증용
    public Authentication getAuthentication(String token){
        Claims claims = getClaims(token);
        // 사용자의 권한을 GrantedAuthority 객체로 관리 => 사용자의 권한(Role) 정보를 나타냄
        Set<SimpleGrantedAuthority> authorities = Collections.singleton(
                new SimpleGrantedAuthority("ROLE_USER"));

        // principal, credentials, Collection<? extends GrantedAuthority> authorities
        return new UsernamePasswordAuthenticationToken(
                new org.springframework.security.core.userdetails.User(
                        // username, password, Collection<? extends GrantedAuthority> authorities
                        claims.getSubject(), "", authorities), token, authorities
        );
    }
    // 토큰 기반으로 유저 ID 를 가져오는 메서드
    public Long getUserId(String token){
        Claims claims = getClaims(token);
        return claims.get("id", Long.class); // "id" 로 되어있는 값을 Long 타입으로 가져오기
    }
    // 토큰을 검증한 뒤 body 에 담아 반환하기
    private Claims getClaims(String token){
        return Jwts.parser()
                .setSigningKey(jwtProperties.getSecretKey())
                .parseClaimsJws(token)
                .getBody();
    }
}

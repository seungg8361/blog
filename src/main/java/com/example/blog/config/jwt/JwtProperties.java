package com.example.blog.config.jwt;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component // 빈으로 등록
@ConfigurationProperties("jwt") // 자바 클래스에 프로피티값을 가져와서 사용하는 애너테이션 => jwt: issuer:, secret_key
public class JwtProperties {

    private String issuer;
    private String secretKey;
}

package com.example.blog.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor // 접근 제어자가 protected 인 기본 생성자
public class Article {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 기본키를 자동으로 1씩 증가
    @Column(name = "id", updatable = false)
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "content", nullable = false)
    private String content;

    @Builder   // 빌더 패턴으로 객체 생성 -> 객체를 유연하고 직관적으로 생성하기 위해
    public Article(String title, String content) {
        this.title = title;
        this.content = content;
    }
}

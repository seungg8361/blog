package com.example.blog.dto;

import com.example.blog.entity.Article;
import lombok.Getter;

@Getter
public class ArticleResponse {

    private final String title;
    private final String content;

    // 실제 데이터베이스를 조회할 수 있게 Article 객체의 데이터를 가져와서 ArticleResponse 로 변환
    public ArticleResponse(Article article) {
        this.title = article.getTitle();
        this.content = article.getContent();
    }
}

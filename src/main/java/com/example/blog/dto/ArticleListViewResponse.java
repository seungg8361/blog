package com.example.blog.dto;

import com.example.blog.entity.Article;
import lombok.Getter;

@Getter
public class ArticleListViewResponse {  // 작성된 모든 글을 응답하는 dto

    private final Long id;
    private final String title;
    private final String content;

    public ArticleListViewResponse(Article article) {
        this.id = article.getId();
        this.title = article.getTitle();
        this.content = article.getContent();
    }
}

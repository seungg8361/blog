package com.example.blog.dto;

import com.example.blog.entity.Article;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor // 모든 필드 값을 파라미터로 받는 생성자
public class AddArticleRequest {

    private String title;
    private String content;

    // 빌더 패턴을 사용해 dto 를 엔티티로 만들어줌. 추후에 블로그 글을 추가할 때 저장할 엔티티로 변환하는 용도
    // 엔티티로 직접 데이터를 관리하지 않기 위해 사용함.
    public Article toEntity(){
        return Article.builder()
                .title(title)
                .content(content)
                .build();
    }
}

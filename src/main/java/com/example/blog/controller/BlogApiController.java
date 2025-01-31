package com.example.blog.controller;

import com.example.blog.dto.AddArticleRequest;
import com.example.blog.dto.ArticleResponse;
import com.example.blog.entity.Article;
import com.example.blog.service.BlogService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController // HTTP Response Body 에 객체 데이터를 JSON 형식으로 반환하는 컨트롤러
@RequiredArgsConstructor
public class BlogApiController {

    private final BlogService blogService;

    @PostMapping("/api/articles")
    // @RequestBody => 클라이언트에서 보낸 JSON 형식의 데이터를 Java 객체로 변환
    public ResponseEntity<Article> addArticle(@RequestBody AddArticleRequest request){
        Article savedArticle = blogService.save(request);

        // 요청한 자원이 성공적으로 생성되었으며, 글 정보를 응답 객체에 담아 JSON 형식으로 전송
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(savedArticle);
    }

    @GetMapping("/api/articles")
    public ResponseEntity<List<ArticleResponse>> findAllArticles(){
        List<ArticleResponse> articles = blogService.findAll()
                .stream() // Java 8 Stream API 를 사용하여 스트림을 생성하여 데이터를 쉽게 변환하고 필터링 함.
                /*
                 map()은 스트림의 각 요소를 변환하는 연산. 여기서는 Article 객체를 ArticleResponse 객체로 변환하고 있습니다.
                 ArticleResponse::new 는 메서드 참조로, 각 Article 객체를 ArticleResponse 객체로 변환하는 생성자를 호출한다.
                 Article 엔티티의 데이터를 ArticleResponse 로 변환하여 필요한 데이터만 전달할 수 있다.
                */
                .map(ArticleResponse::new)
                .toList(); // 스트림에서 변환된 결과를 다시 리스트 형태로 변환

        return ResponseEntity.ok()
                .body(articles);
    }

    @GetMapping("/api/articles/{id}")
    // @PathVariable => URL 에서 값을 가져오는 애너테이션
    public ResponseEntity<ArticleResponse> findArticle(@PathVariable Long id){
       Article article = blogService.findById(id);

       return ResponseEntity.ok()
               .body(new ArticleResponse(article));
    }

    @DeleteMapping("/api/articles/{id}")
    public ResponseEntity<Void> deleteArticle(@PathVariable Long id){
        blogService.delete(id);

        return ResponseEntity.ok()
                .build();
    }
}

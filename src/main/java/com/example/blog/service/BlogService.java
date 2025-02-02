package com.example.blog.service;

import com.example.blog.dto.AddArticleRequest;
import com.example.blog.dto.UpdateArticleRequest;
import com.example.blog.entity.Article;
import com.example.blog.repository.BlogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor // final, @NotNull 이 붙은 필드의 생성자를 추가
public class BlogService {

    private final BlogRepository blogRepository;

    /*
        JpaRepository 에서 지원하는 save() 를 사용해서 글 저장하는 메서드
        엔티티로 직접 데이터를 저장하지 않기 위해 만들었둔 AddArticleRequest 의 toEntity() 사용.
    */
    public Article save(AddArticleRequest request){
        return blogRepository.save(request.toEntity());
    }

    public List<Article> findAll(){
        return blogRepository.findAll();
    }

    public Article findById(Long id){
        return blogRepository.findById(id)
                .orElseThrow(()-> new IllegalArgumentException("not found : " + id));
    }

    public void delete(Long id){
        blogRepository.deleteById(id);
    }

    @Transactional
    public Article update(Long id, UpdateArticleRequest request){
        Article article = blogRepository.findById(id)
                .orElseThrow(()-> new IllegalArgumentException("not found : " + id));

        article.update(request.getTitle(), request.getContent());
        return article;
    }
}

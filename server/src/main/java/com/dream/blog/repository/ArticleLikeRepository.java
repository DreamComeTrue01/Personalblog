package com.dream.blog.repository;

import com.dream.blog.model.ArticleLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ArticleLikeRepository extends JpaRepository<ArticleLike, Integer> {
    Optional<ArticleLike> findByArticleIdAndUserIp(Integer articleId, String userIp);
    boolean existsByArticleIdAndUserIp(Integer articleId, String userIp);
    void deleteByArticleIdAndUserIp(Integer articleId, String userIp);
}

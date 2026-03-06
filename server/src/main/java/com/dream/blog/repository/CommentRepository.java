package com.dream.blog.repository;

import com.dream.blog.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {
    List<Comment> findByArticleIdOrderByCreatedAtDesc(Integer articleId);
    long countByArticleId(Integer articleId);
}

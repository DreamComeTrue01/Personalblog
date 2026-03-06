package com.dream.blog.service;

import com.dream.blog.model.Comment;
import com.dream.blog.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentService {
    @Autowired
    private CommentRepository commentRepository;
    
    public List<Comment> getCommentsByArticleId(Integer articleId) {
        return commentRepository.findByArticleIdOrderByCreatedAtDesc(articleId);
    }
    
    public long getCommentCountByArticleId(Integer articleId) {
        return commentRepository.countByArticleId(articleId);
    }
    
    public Comment createComment(Comment comment) {
        return commentRepository.save(comment);
    }
    
    public void deleteComment(Integer id) {
        commentRepository.deleteById(id);
    }
}

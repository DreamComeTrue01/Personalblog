package com.dream.blog.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "article_likes")
public class ArticleLike {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @Column(name = "article_id", nullable = false)
    private Integer articleId;
    
    @Column(name = "user_ip", nullable = false)
    private String userIp;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    public ArticleLike() {
        this.createdAt = LocalDateTime.now();
    }
    
    public ArticleLike(Integer articleId, String userIp) {
        this.articleId = articleId;
        this.userIp = userIp;
        this.createdAt = LocalDateTime.now();
    }
    
    public Integer getId() {
        return id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    
    public Integer getArticleId() {
        return articleId;
    }
    
    public void setArticleId(Integer articleId) {
        this.articleId = articleId;
    }
    
    public String getUserIp() {
        return userIp;
    }
    
    public void setUserIp(String userIp) {
        this.userIp = userIp;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}

package com.dream.blog.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "life_post_likes")
public class LifePostLike {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @Column(name = "life_post_id", nullable = false)
    private Integer lifePostId;
    
    @Column(name = "user_ip", nullable = false)
    private String userIp;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    public LifePostLike() {
        this.createdAt = LocalDateTime.now();
    }
    
    public LifePostLike(Integer lifePostId, String userIp) {
        this.lifePostId = lifePostId;
        this.userIp = userIp;
        this.createdAt = LocalDateTime.now();
    }
    
    public Integer getId() {
        return id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    
    public Integer getLifePostId() {
        return lifePostId;
    }
    
    public void setLifePostId(Integer lifePostId) {
        this.lifePostId = lifePostId;
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

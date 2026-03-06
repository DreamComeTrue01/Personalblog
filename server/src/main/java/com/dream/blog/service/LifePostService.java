package com.dream.blog.service;

import com.dream.blog.model.LifePost;
import com.dream.blog.model.LifePostLike;
import com.dream.blog.model.LifeReply;
import com.dream.blog.repository.LifePostLikeRepository;
import com.dream.blog.repository.LifePostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class LifePostService {
    @Autowired
    private LifePostRepository lifePostRepository;
    
    @Autowired
    private LifePostLikeRepository lifePostLikeRepository;
    
    public List<LifePost> getAllLifePosts() {
        return lifePostRepository.findAll();
    }
    
    public Optional<LifePost> getLifePostById(Integer id) {
        return lifePostRepository.findById(id);
    }
    
    public LifePost createLifePost(LifePost lifePost) {
        return lifePostRepository.save(lifePost);
    }
    
    public LifePost updateLifePost(Integer id, LifePost lifePost) {
        lifePost.setId(id);
        return lifePostRepository.save(lifePost);
    }
    
    public void deleteLifePost(Integer id) {
        lifePostRepository.deleteById(id);
    }
    
    public LifePost incrementViews(Integer id) {
        LifePost lifePost = lifePostRepository.findById(id).orElse(null);
        if (lifePost != null) {
            lifePost.setViews(lifePost.getViews() + 1);
            return lifePostRepository.save(lifePost);
        }
        return null;
    }
    
    public Map<String, Object> toggleLike(Integer id, String userIp) {
        LifePost lifePost = lifePostRepository.findById(id).orElse(null);
        if (lifePost != null) {
            // 检查用户是否已经点赞
            boolean hasLiked = lifePostLikeRepository.existsByLifePostIdAndUserIp(id, userIp);
            
            if (hasLiked) {
                // 如果已经点赞，则取消点赞
                lifePostLikeRepository.deleteByLifePostIdAndUserIp(id, userIp);
                lifePost.setLikes(Math.max(0, lifePost.getLikes() - 1));
                lifePost = lifePostRepository.save(lifePost);
                Map<String, Object> result = new HashMap<>();
                result.put("likes", lifePost.getLikes());
                result.put("isLiked", false);
                return result;
            } else {
                // 如果未点赞，则添加点赞
                LifePostLike lifePostLike = new LifePostLike(id, userIp);
                lifePostLikeRepository.save(lifePostLike);
                lifePost.setLikes(lifePost.getLikes() + 1);
                lifePost = lifePostRepository.save(lifePost);
                Map<String, Object> result = new HashMap<>();
                result.put("likes", lifePost.getLikes());
                result.put("isLiked", true);
                return result;
            }
        }
        return null;
    }
    
    public boolean hasUserLiked(Integer lifePostId, String userIp) {
        return lifePostLikeRepository.existsByLifePostIdAndUserIp(lifePostId, userIp);
    }
    
    public LifePost addReply(Integer id, LifeReply reply) {
        LifePost lifePost = lifePostRepository.findById(id).orElse(null);
        if (lifePost != null) {
            reply.setLifePost(lifePost);
            if (lifePost.getReplies() == null) {
                lifePost.setReplies(new ArrayList<>());
            }
            lifePost.getReplies().add(reply);
            return lifePostRepository.save(lifePost);
        }
        return null;
    }
}
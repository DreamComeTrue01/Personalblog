package com.dream.blog.service;

import com.dream.blog.model.Article;
import com.dream.blog.model.ArticleLike;
import com.dream.blog.repository.ArticleLikeRepository;
import com.dream.blog.repository.ArticleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class ArticleService {
    @Autowired
    private ArticleRepository articleRepository;
    
    @Autowired
    private ArticleLikeRepository articleLikeRepository;
    
    public List<Article> getAllArticles() {
        return articleRepository.findAll();
    }
    
    public List<Article> getArchivedArticles() {
        return articleRepository.findByIsArchivedTrue();
    }
    
    public Optional<Article> getArticleById(Integer id) {
        return articleRepository.findById(id);
    }
    
    public Article createArticle(Article article) {
        return articleRepository.save(article);
    }
    
    public Article updateArticle(Integer id, Article article) {
        article.setId(id);
        return articleRepository.save(article);
    }
    
    public void deleteArticle(Integer id) {
        articleRepository.deleteById(id);
    }
    
    public Article incrementViews(Integer id) {
        Article article = articleRepository.findById(id).orElse(null);
        if (article != null) {
            article.setViews(article.getViews() + 1);
            return articleRepository.save(article);
        }
        return null;
    }
    
    public Map<String, Object> toggleLike(Integer id, String userIp) {
        Article article = articleRepository.findById(id).orElse(null);
        if (article != null) {
            // 检查用户是否已经点赞
            boolean hasLiked = articleLikeRepository.existsByArticleIdAndUserIp(id, userIp);
            
            if (hasLiked) {
                // 如果已经点赞，则取消点赞
                articleLikeRepository.deleteByArticleIdAndUserIp(id, userIp);
                article.setLikes(Math.max(0, article.getLikes() - 1));
                article = articleRepository.save(article);
                Map<String, Object> result = new HashMap<>();
                result.put("likes", article.getLikes());
                result.put("isLiked", false);
                result.put("message", "取消点赞成功");
                return result;
            } else {
                // 如果未点赞，则添加点赞
                ArticleLike articleLike = new ArticleLike(id, userIp);
                articleLikeRepository.save(articleLike);
                article.setLikes(article.getLikes() + 1);
                article = articleRepository.save(article);
                Map<String, Object> result = new HashMap<>();
                result.put("likes", article.getLikes());
                result.put("isLiked", true);
                result.put("message", "点赞成功");
                return result;
            }
        }
        return null;
    }
    
    public boolean hasUserLiked(Integer articleId, String userIp) {
        return articleLikeRepository.existsByArticleIdAndUserIp(articleId, userIp);
    }
}
package com.dream.blog.controller;

import com.dream.blog.model.Article;
import com.dream.blog.model.Comment;
import com.dream.blog.model.LifePost;
import com.dream.blog.model.LifeReply;
import com.dream.blog.model.Message;
import com.dream.blog.service.ArticleService;
import com.dream.blog.service.CommentService;
import com.dream.blog.service.LifePostService;
import com.dream.blog.service.MessageService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class ApiController {
    @Autowired
    private ArticleService articleService;
    
    @Autowired
    private LifePostService lifePostService;
    
    @Autowired
    private MessageService messageService;
    
    @Autowired
    private CommentService commentService;
    
    // 文章相关接口
    @GetMapping("/articles")
    public ResponseEntity<List<Article>> getAllArticles() {
        return ResponseEntity.ok(articleService.getAllArticles());
    }
    
    @GetMapping("/articles/{id}")
    public ResponseEntity<Article> getArticleById(@PathVariable Integer id) {
        return articleService.getArticleById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping("/articles")
    public ResponseEntity<Article> createArticle(@RequestBody Article article) {
        return ResponseEntity.status(HttpStatus.CREATED).body(articleService.createArticle(article));
    }
    
    @PutMapping("/articles/{id}")
    public ResponseEntity<Article> updateArticle(@PathVariable Integer id, @RequestBody Article article) {
        return ResponseEntity.ok(articleService.updateArticle(id, article));
    }
    
    @DeleteMapping("/articles/{id}")
    public ResponseEntity<Void> deleteArticle(@PathVariable Integer id) {
        articleService.deleteArticle(id);
        return ResponseEntity.noContent().build();
    }
    
    @PostMapping("/articles/{id}/views")
    public ResponseEntity<Article> incrementArticleViews(@PathVariable Integer id) {
        Article article = articleService.incrementViews(id);
        return article != null ? ResponseEntity.ok(article) : ResponseEntity.notFound().build();
    }
    
    @PostMapping("/articles/{id}/like")
    public ResponseEntity<Map<String, Object>> toggleArticleLike(@PathVariable Integer id, 
            @RequestHeader(value = "X-Forwarded-For", required = false) String xForwardedFor,
            @RequestHeader(value = "X-Real-IP", required = false) String xRealIp,
            HttpServletRequest request) {
        // 获取用户IP地址
        String userIp = xForwardedFor;
        if (userIp == null || userIp.isEmpty()) {
            userIp = xRealIp;
        }
        if (userIp == null || userIp.isEmpty()) {
            userIp = request.getRemoteAddr();
        }
        // 如果IP包含多个地址（通过代理），取第一个
        if (userIp != null && userIp.contains(",")) {
            userIp = userIp.split(",")[0].trim();
        }
        
        Map<String, Object> result = articleService.toggleLike(id, userIp);
        return result != null ? ResponseEntity.ok(result) : ResponseEntity.notFound().build();
    }
    
    @GetMapping("/articles/{id}/like/status")
    public ResponseEntity<Map<String, Object>> getArticleLikeStatus(@PathVariable Integer id,
            @RequestHeader(value = "X-Forwarded-For", required = false) String xForwardedFor,
            @RequestHeader(value = "X-Real-IP", required = false) String xRealIp,
            HttpServletRequest request) {
        // 获取用户IP地址
        String userIp = xForwardedFor;
        if (userIp == null || userIp.isEmpty()) {
            userIp = xRealIp;
        }
        if (userIp == null || userIp.isEmpty()) {
            userIp = request.getRemoteAddr();
        }
        if (userIp != null && userIp.contains(",")) {
            userIp = userIp.split(",")[0].trim();
        }
        
        boolean hasLiked = articleService.hasUserLiked(id, userIp);
        Map<String, Object> result = new HashMap<>();
        result.put("hasLiked", hasLiked);
        return ResponseEntity.ok(result);
    }
    
    // 评论相关接口
    @GetMapping("/articles/{id}/comments")
    public ResponseEntity<Map<String, Object>> getArticleComments(@PathVariable Integer id) {
        List<Comment> comments = commentService.getCommentsByArticleId(id);
        long commentCount = commentService.getCommentCountByArticleId(id);
        Map<String, Object> result = new HashMap<>();
        result.put("comments", comments);
        result.put("commentCount", commentCount);
        return ResponseEntity.ok(result);
    }
    
    @PostMapping("/articles/{id}/comments")
    public ResponseEntity<Map<String, Object>> createArticleComment(@PathVariable Integer id, @RequestBody Comment comment) {
        comment.setArticleId(id);
        Comment savedComment = commentService.createComment(comment);
        long commentCount = commentService.getCommentCountByArticleId(id);
        Map<String, Object> result = new HashMap<>();
        result.put("comment", savedComment);
        result.put("commentCount", commentCount);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }
    
    // 碎碎念相关接口
    @GetMapping("/life")
    public ResponseEntity<List<LifePost>> getAllLifePosts() {
        return ResponseEntity.ok(lifePostService.getAllLifePosts());
    }
    
    @GetMapping("/life/{id}")
    public ResponseEntity<LifePost> getLifePostById(@PathVariable Integer id) {
        return lifePostService.getLifePostById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping("/life")
    public ResponseEntity<LifePost> createLifePost(@RequestBody LifePost lifePost) {
        return ResponseEntity.status(HttpStatus.CREATED).body(lifePostService.createLifePost(lifePost));
    }
    
    @PutMapping("/life/{id}")
    public ResponseEntity<LifePost> updateLifePost(@PathVariable Integer id, @RequestBody LifePost lifePost) {
        return ResponseEntity.ok(lifePostService.updateLifePost(id, lifePost));
    }
    
    @DeleteMapping("/life/{id}")
    public ResponseEntity<Void> deleteLifePost(@PathVariable Integer id) {
        lifePostService.deleteLifePost(id);
        return ResponseEntity.noContent().build();
    }
    
    @PostMapping("/life/{id}/views")
    public ResponseEntity<LifePost> incrementLifePostViews(@PathVariable Integer id) {
        LifePost lifePost = lifePostService.incrementViews(id);
        return lifePost != null ? ResponseEntity.ok(lifePost) : ResponseEntity.notFound().build();
    }
    
    @PostMapping("/life/{id}/like")
    public ResponseEntity<Map<String, Object>> toggleLifePostLike(@PathVariable Integer id, 
            @RequestHeader(value = "X-Forwarded-For", required = false) String xForwardedFor,
            @RequestHeader(value = "X-Real-IP", required = false) String xRealIp,
            HttpServletRequest request) {
        // 获取用户IP地址
        String userIp = xForwardedFor;
        if (userIp == null || userIp.isEmpty()) {
            userIp = xRealIp;
        }
        if (userIp == null || userIp.isEmpty()) {
            userIp = request.getRemoteAddr();
        }
        // 如果IP包含多个地址（通过代理），取第一个
        if (userIp != null && userIp.contains(",")) {
            userIp = userIp.split(",")[0].trim();
        }
        
        Map<String, Object> result = lifePostService.toggleLike(id, userIp);
        return result != null ? ResponseEntity.ok(result) : ResponseEntity.notFound().build();
    }
    
    @PostMapping("/life/{id}/replies")
    public ResponseEntity<LifePost> addLifePostReply(@PathVariable Integer id, @RequestBody LifeReply reply) {
        LifePost lifePost = lifePostService.addReply(id, reply);
        return lifePost != null ? ResponseEntity.status(HttpStatus.CREATED).body(lifePost) : ResponseEntity.notFound().build();
    }
    
    @GetMapping("/life/{id}/like/status")
    public ResponseEntity<Map<String, Object>> getLifePostLikeStatus(@PathVariable Integer id,
            @RequestHeader(value = "X-Forwarded-For", required = false) String xForwardedFor,
            @RequestHeader(value = "X-Real-IP", required = false) String xRealIp,
            HttpServletRequest request) {
        // 获取用户IP地址
        String userIp = xForwardedFor;
        if (userIp == null || userIp.isEmpty()) {
            userIp = xRealIp;
        }
        if (userIp == null || userIp.isEmpty()) {
            userIp = request.getRemoteAddr();
        }
        if (userIp != null && userIp.contains(",")) {
            userIp = userIp.split(",")[0].trim();
        }
        
        boolean hasLiked = lifePostService.hasUserLiked(id, userIp);
        Map<String, Object> result = new HashMap<>();
        result.put("hasLiked", hasLiked);
        return ResponseEntity.ok(result);
    }
    
    // 留言相关接口
    @GetMapping("/messages")
    public ResponseEntity<List<Message>> getAllMessages() {
        return ResponseEntity.ok(messageService.getAllMessages());
    }
    
    @PostMapping("/messages")
    public ResponseEntity<Message> createMessage(@RequestBody Message message) {
        return ResponseEntity.status(HttpStatus.CREATED).body(messageService.createMessage(message));
    }
    
    @DeleteMapping("/messages/{id}")
    public ResponseEntity<Void> deleteMessage(@PathVariable Integer id) {
        messageService.deleteMessage(id);
        return ResponseEntity.noContent().build();
    }
}
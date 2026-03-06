package com.dream.blog.repository;

import com.dream.blog.model.LifePostLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LifePostLikeRepository extends JpaRepository<LifePostLike, Integer> {
    boolean existsByLifePostIdAndUserIp(Integer lifePostId, String userIp);
    void deleteByLifePostIdAndUserIp(Integer lifePostId, String userIp);
}

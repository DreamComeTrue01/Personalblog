package com.dream.blog.repository;

import com.dream.blog.model.LifePost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LifePostRepository extends JpaRepository<LifePost, Integer> {
}
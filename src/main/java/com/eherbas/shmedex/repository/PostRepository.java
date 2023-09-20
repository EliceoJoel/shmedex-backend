package com.eherbas.shmedex.repository;

import com.eherbas.shmedex.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
}

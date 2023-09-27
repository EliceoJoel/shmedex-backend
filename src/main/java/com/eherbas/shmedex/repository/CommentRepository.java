package com.eherbas.shmedex.repository;

import com.eherbas.shmedex.model.Comment;
import com.eherbas.shmedex.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByPost(Post post);
}

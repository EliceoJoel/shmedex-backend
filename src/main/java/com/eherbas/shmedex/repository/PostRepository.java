package com.eherbas.shmedex.repository;

import com.eherbas.shmedex.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    @Query("SELECT p, CONCAT(u.name, ' ', u.lastName), SIZE(p.usersWhoFollows), SIZE(p.comments) FROM Post p JOIN p.user u WHERE p IN (SELECT p2 FROM User u2 JOIN u2.followedPosts p2 WHERE u2.id = :userId)")
    List<Object[]> findPostsFollowedByUserWithUserName(@Param("userId") Long userId);

    @Query("SELECT p, CONCAT(u.name, ' ', u.lastName), SIZE(p.usersWhoFollows), SIZE(p.comments) FROM Post p, User u WHERE p.user = u AND u.id <> :userId AND p NOT IN (SELECT p2 FROM User u2 JOIN u2.followedPosts p2 WHERE u2.id = :userId)")
    List<Object[]> findPostsNotFollowedByUserWithUserName(@Param("userId") Long userId);
}

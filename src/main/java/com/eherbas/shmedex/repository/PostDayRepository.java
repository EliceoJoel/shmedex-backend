package com.eherbas.shmedex.repository;

import com.eherbas.shmedex.model.Post;
import com.eherbas.shmedex.model.PostDay;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostDayRepository extends JpaRepository<PostDay, Long> {
    PostDay findPostDayByPostAndDay(Post post, Integer day);
    List<PostDay> findPostDaysByPost(Post post);
    @Query("SELECT pd.day FROM PostDay pd WHERE pd.post.id = :postId")
    List<Integer> findDaysByPostId(@Param("postId") Long postId);
}

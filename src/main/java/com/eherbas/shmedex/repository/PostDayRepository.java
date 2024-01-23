package com.eherbas.shmedex.repository;

import com.eherbas.shmedex.model.Post;
import com.eherbas.shmedex.model.PostDay;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostDayRepository extends JpaRepository<PostDay, Long> {
    PostDay findPostDayByPostAndDay(Post post, Integer day);
    List<PostDay> findPostDaysByPost(Post post);
}

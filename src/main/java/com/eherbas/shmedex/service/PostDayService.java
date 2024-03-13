package com.eherbas.shmedex.service;

import com.eherbas.shmedex.dto.NewPostDayDTO;
import com.eherbas.shmedex.dto.PostDTO;
import com.eherbas.shmedex.dto.PostDayDTO;

import java.util.List;
import java.util.Optional;

public interface PostDayService {
    PostDayDTO save(PostDayDTO postDayDTO);
    PostDayDTO add(PostDTO postDTO, NewPostDayDTO newPostDayDTO);
    Optional<PostDayDTO> getById(Long id);
    List<PostDayDTO> getAllByPostId(Long postId);
    List<Integer> getDaysByPostId(Long postId);
    void deleteById(Long id);
    PostDayDTO getPostDayByPostAndDay(PostDTO post, Integer day);
}

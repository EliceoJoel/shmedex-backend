package com.eherbas.shmedex.service;

import com.eherbas.shmedex.dto.PostDayDTO;

import java.util.List;
import java.util.Optional;

public interface PostDayService {

    Optional<PostDayDTO> getById(Long id);
    List<PostDayDTO> getAllByPostId(Long postId);
    List<Integer> getDaysByPostId(Long postId);
    void deleteById(Long id);
}

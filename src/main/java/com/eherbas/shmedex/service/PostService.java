package com.eherbas.shmedex.service;

import com.eherbas.shmedex.dto.NewPostDTO;
import com.eherbas.shmedex.dto.PostDTO;
import com.eherbas.shmedex.dto.UserDTO;
import com.eherbas.shmedex.model.DetailedPostDTO;
import com.eherbas.shmedex.model.Post;
import com.eherbas.shmedex.model.User;

import java.util.List;
import java.util.Optional;

public interface PostService {
    PostDTO save(NewPostDTO newPostDTO);
    Optional<PostDTO> getById(Long postId);
    DetailedPostDTO getDetailedPost(PostDTO post, UserDTO user, Integer postDay);
    void deleteById(Long postId);
    void toggleLike(Long postId, Long userId);
    void toggleFollow(Long postId, Long userId);
    List<DetailedPostDTO> getAllFollowedByUser(Long userId);
    List<DetailedPostDTO> getAllNotFollowedByUser(Long userId);
}

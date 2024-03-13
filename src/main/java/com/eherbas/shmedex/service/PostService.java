package com.eherbas.shmedex.service;

import com.eherbas.shmedex.dto.NewPostDTO;
import com.eherbas.shmedex.dto.PostDTO;
import com.eherbas.shmedex.dto.UserDTO;
import com.eherbas.shmedex.model.DetailedPostDTO;

import java.util.List;
import java.util.Optional;

public interface PostService {
    PostDTO save(NewPostDTO newPostDTO);
    Optional<PostDTO> getById(Long postId);
    DetailedPostDTO getDetailedPost(PostDTO post, UserDTO user, Integer postDay);
    void deletePost(PostDTO post);
    String toggleLike(PostDTO post, UserDTO user);
    String toggleFollow(PostDTO post, UserDTO user);
    List<DetailedPostDTO> getAllFollowedByUser(UserDTO user);
    List<DetailedPostDTO> getAllNotFollowedByUser(UserDTO user);
    List<DetailedPostDTO> getUserPosts(UserDTO user);
}

package com.eherbas.shmedex.service;

import com.eherbas.shmedex.dto.*;

import java.util.List;
import java.util.Optional;

public interface PostService {
    PostDTO save(NewPostDTO newPostDTO);
    Optional<PostDTO> getById(Long postId);
    DetailedPostDTO getDetailedPost(PostDTO postDTO, UserDTO userDTO, Integer postDay);
    void deletePost(PostDTO postDTO);
    String toggleLike(PostDTO postDTO, UserDTO userDTO);
    String toggleFollow(PostDTO postDTO, UserDTO userDTO);
    List<DetailedPostDTO> getAllFollowedByUser(UserDTO userDTO);
    List<DetailedPostDTO> getAllNotFollowedByUser(UserDTO userDTO);
    List<DetailedPostDTO> getUserPosts(UserDTO userDTO);
    CommentDTO addComment(PostDTO postDTO, CommentDTO commentDTO);
    List<CommentWithUserDTO> getPostComments(PostDTO postDTO);
}

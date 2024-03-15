package com.eherbas.shmedex.mapper;

import com.eherbas.shmedex.dto.PostDTO;
import com.eherbas.shmedex.model.Comment;
import com.eherbas.shmedex.model.Post;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class PostMapper implements CustomMapper<PostDTO, Post>{
    @Override
    public PostDTO toDto(Post post) {
        PostDTO postDTO = new PostDTO();
        postDTO.setId(post.getId());
        postDTO.setUser(post.getUser());
        postDTO.setComments(post.getComments());
        postDTO.setPostDays(post.getPostDays());
        postDTO.setUserWhoLikes(post.getUserWhoLikes());
        postDTO.setUsersWhoFollows(post.getUsersWhoFollows());
        return postDTO;
    }

    @Override
    public Post toEntity(PostDTO postDTO) {
        Post post = new Post();
        post.setId(postDTO.getId());
        post.setUser(postDTO.getUser());
        post.setComments(postDTO.getComments() != null ? postDTO.getComments() : new ArrayList<>());
        post.setPostDays(postDTO.getPostDays()  != null ? postDTO.getPostDays() : new ArrayList<>());
        post.setUserWhoLikes(postDTO.getUserWhoLikes() != null ? postDTO.getUserWhoLikes() : new ArrayList<>());
        post.setUsersWhoFollows(postDTO.getUsersWhoFollows() != null ? postDTO.getUsersWhoFollows() : new ArrayList<>());
        return post;
    }
}

package com.eherbas.shmedex.mapper;

import com.eherbas.shmedex.dto.PostDTO;
import com.eherbas.shmedex.model.Post;
import org.springframework.stereotype.Component;

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
        post.setComments(postDTO.getComments());
        post.setPostDays(postDTO.getPostDays());
        post.setUserWhoLikes(postDTO.getUserWhoLikes());
        post.setUsersWhoFollows(post.getUsersWhoFollows());
        return post;
    }
}

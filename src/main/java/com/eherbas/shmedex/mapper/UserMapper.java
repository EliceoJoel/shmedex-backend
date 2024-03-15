package com.eherbas.shmedex.mapper;

import com.eherbas.shmedex.dto.UserDTO;
import com.eherbas.shmedex.model.User;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class UserMapper implements CustomMapper<UserDTO, User>{
    @Override
    public UserDTO toDto(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setName(user.getName());
        userDTO.setLastName(user.getLastName());
        userDTO.setEmail(user.getEmail());
        userDTO.setPassword(user.getPassword());
        userDTO.setPosts(user.getPosts());
        userDTO.setFollowedPosts(user.getFollowedPosts());
        userDTO.setLikedPosts(user.getLikedPosts());
        userDTO.setComments(user.getComments());
        return userDTO;
    }

    @Override
    public User toEntity(UserDTO userDTO) {
        User user = new User();
        user.setId(userDTO.getId());
        user.setName(userDTO.getName());
        user.setLastName(userDTO.getLastName());
        user.setEmail(userDTO.getEmail());
        user.setPassword(userDTO.getPassword());
        user.setPosts(userDTO.getPosts() != null ? userDTO.getPosts() : new ArrayList<>());
        user.setFollowedPosts(userDTO.getFollowedPosts() != null ? userDTO.getFollowedPosts() : new ArrayList<>());
        user.setLikedPosts(userDTO.getLikedPosts() != null ? userDTO.getLikedPosts() : new ArrayList<>());
        user.setComments(userDTO.getComments() != null ? userDTO.getComments() : new ArrayList<>());
        return user;
    }
}

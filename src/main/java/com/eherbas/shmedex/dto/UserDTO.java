package com.eherbas.shmedex.dto;

import com.eherbas.shmedex.model.Comment;
import com.eherbas.shmedex.model.Post;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    private long id;
    private String name;
    private String lastName;
    private String email;
    private String password;
    private List<Post> posts;
    private List<Post> followedPosts;
    private List<Post> likedPosts;
    private List<Comment> comments;
}

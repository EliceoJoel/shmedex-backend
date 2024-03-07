package com.eherbas.shmedex.dto;

import com.eherbas.shmedex.model.Comment;
import com.eherbas.shmedex.model.PostDay;
import com.eherbas.shmedex.model.User;
import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostDTO {
    private long id;
    private User user;
    private List<Comment> comments;
    private List<PostDay> postDays;
    private List<User> usersWhoFollows;
    private List<User> userWhoLikes;
}

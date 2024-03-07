package com.eherbas.shmedex.dto;

import com.eherbas.shmedex.model.Post;
import com.eherbas.shmedex.model.User;
import lombok.*;

import java.time.ZonedDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentDTO {
    private long id;
    private String content;
    private ZonedDateTime createdAt;
    private Post post;
    private User user;
}

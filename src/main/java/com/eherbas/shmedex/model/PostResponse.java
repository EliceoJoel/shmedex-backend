package com.eherbas.shmedex.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PostResponse {
    private Post post;
    private String userName;
    private Integer numberOfFollowers;
    private Integer numberOfComments;
    private Integer numberOfLikes;
    private Boolean isFollowedByUser;
    private Boolean isLikedByUser;
    private PostDay postDay;
}

package com.eherbas.shmedex.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DetailedPostDTO {
    private Post post;
    private String userName;
    private Integer numberOfFollowers;
    private Integer numberOfComments;
    private Integer numberOfLikes;
    private Boolean isFollowedByUser;
    private Boolean isLikedByUser;
    private Boolean isUserPost;
    private List<PostDay> postDays;
}

package com.eherbas.shmedex.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PostWithUserName {
    private Post post;
    private String userName;
    private Integer numberOfFollowers;
    private Integer numberOfComments;
}

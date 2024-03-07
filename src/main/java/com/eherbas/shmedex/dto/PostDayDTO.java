package com.eherbas.shmedex.dto;

import com.eherbas.shmedex.model.Post;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostDayDTO {
    private Long id;
    private Integer day;
    private String content;
    private String image;
    private ZonedDateTime createdAt;
    private ZonedDateTime  updatedAt;
    private Post post;
}

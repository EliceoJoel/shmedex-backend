package com.eherbas.shmedex.dto;

import com.eherbas.shmedex.model.Comment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentWithUserDTO {
    Comment comment;
    String userWhoCommented;
}

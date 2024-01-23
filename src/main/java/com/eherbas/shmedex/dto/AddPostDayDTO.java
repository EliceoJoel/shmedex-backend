package com.eherbas.shmedex.dto;

import com.eherbas.shmedex.model.PostDay;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AddPostDayDTO {
    private Long postId;
    private PostDay postDay;
}
